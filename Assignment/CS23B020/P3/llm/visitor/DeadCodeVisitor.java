package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 5: Liveness Analysis + Dead Code Marking.
 *
 * Extended method elimination rule:
 *
 * A reachable method foo can be eliminated when ALL of:
 * (a) Its return value is a known constant (cp.getReturnValue is CONST)
 * (b) It has no side effects (no println, no field writes)
 * (c) Every call site x = obj.foo(...) has x dead after CP substitution
 * (i.e. the assignment is already dead because x is substituted)
 *
 * Condition (c) is guaranteed by the existing dead-assignment logic:
 * if the call result is always a constant, then in the caller, x gets
 * CONST from evalMessageSend, so x is substituted at every use, so x
 * is never live as a real variable, so the assignment is dead.
 * The void-promotion rule (dead assign with MessageSend RHS -> void call)
 * is SUPPRESSED when the callee has no side effects — we just drop it.
 *
 * Dead-node sets are keyed on INNER concrete nodes (unwrapped from Statement).
 */
public class DeadCodeVisitor extends GJDepthFirst {

    private final ClassHierarchy ch;
    private final SymbolTable st;
    private final CallGraph cg;
    private final ConstPropVisitor cp;

    // Results
    private final Set<Node> deadAssignments = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Map<Node, String> deadIfBranch = new IdentityHashMap<>();
    private final Set<Node> deadWhiles = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<String> deadMethods = new LinkedHashSet<>();
    private final Map<String, Set<String>> everLive = new LinkedHashMap<>();

    // Method AST index
    private final Map<String, Map<String, MethodDeclaration>> methodIndex = new LinkedHashMap<>();
    private MainClass mainClassNode;

    public DeadCodeVisitor(ClassHierarchy ch, SymbolTable st,
            CallGraph cg, ConstPropVisitor cp) {
        this.ch = ch;
        this.st = st;
        this.cg = cg;
        this.cp = cp;
    }

    // ---------------------------------------------------------------
    // Public query API
    // ---------------------------------------------------------------

    public boolean isDeadAssignment(AssignmentStatement n) {
        return deadAssignments.contains(n);
    }

    /**
     * For dead-assignment whose RHS is a MessageSend:
     * should we promote to a void call, or just drop it entirely?
     *
     * Drop entirely (return true) when the callee has no side effects
     * AND its return is a constant — the call is truly useless.
     * Promote to void call (return false) when the callee has side effects.
     */
    public boolean dropMessageSendCall(AssignmentStatement as,
            String callerCls, String callerMeth) {
        if (!(as.f2.f0.choice instanceof MessageSend))
            return false;
        MessageSend ms = (MessageSend) as.f2.f0.choice;
        String methodName = ms.f2.f0.tokenImage;
        String receiverType = resolveReceiverType(ms, callerCls, callerMeth);
        if (receiverType == null)
            return false;

        // Drop if ALL CHA targets have no side effects
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(receiverType);
        candidates.addAll(ch.allSubclasses(receiverType));
        for (String candidate : candidates) {
            String dc2 = ch.declaringClass(candidate, methodName);
            if (dc2 != null && cg.reachable(dc2, methodName))
                if (cp.methodHasSideEffects(dc2, methodName))
                    return false;
        }
        return true;
    }

    /** "else"=keep then, "then"=keep else, null=both live. */
    public String getDeadBranch(IfStatement n) {
        return deadIfBranch.get(n);
    }

    public boolean isDeadWhile(WhileStatement n) {
        return deadWhiles.contains(n);
    }

    public boolean isDeadMethod(String cls, String method) {
        return deadMethods.contains(cls + "::" + method);
    }

    public boolean isDeadVarDecl(String cls, String method, String varName) {
        Set<String> live = everLive.get(cls + "::" + method);
        return live == null || !live.contains(varName);
    }

    // ---------------------------------------------------------------
    // Entry point
    // ---------------------------------------------------------------

    @Override
    public Object visit(Goal n, Object argu) {
        n.f0.accept(this, "INDEX");
        n.f1.accept(this, "INDEX");
        markDeadMethods();
        for (String key : cg.reachableSet()) {
            String[] p = key.split("::");
            analyseMethod(p[0], p[1]);
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Phase 1: indexing
    // ---------------------------------------------------------------

    @Override
    public Object visit(MainClass n, Object argu) {
        mainClassNode = n;
        return null;
    }

    @Override
    public Object visit(ClassDeclaration n, Object argu) {
        if (!"INDEX".equals(argu))
            return null;
        n.f4.accept(this, n.f1.f0.tokenImage);
        return null;
    }

    @Override
    public Object visit(ClassExtendsDeclaration n, Object argu) {
        if (!"INDEX".equals(argu))
            return null;
        n.f6.accept(this, n.f1.f0.tokenImage);
        return null;
    }

    @Override
    public Object visit(MethodDeclaration n, Object argu) {
        if (!(argu instanceof String))
            return null;
        methodIndex.computeIfAbsent((String) argu, k -> new LinkedHashMap<>())
                .put(n.f2.f0.tokenImage, n);
        return null;
    }

    // ---------------------------------------------------------------
    // Phase 2: mark dead methods
    //
    // A method is dead if:
    // (A) Not reachable from CHA call graph, OR
    // (B) Reachable, but return is always a constant AND no side effects
    // (every call site's result will be CP-substituted, so the call
    // itself becomes a dead side-effect-free assignment and is dropped)
    // ---------------------------------------------------------------

    private void markDeadMethods() {
        for (String cls : ch.classNames()) {
            for (String method : ch.methodsOf(cls)) {
                if (!cg.reachable(cls, method)) {
                    // (A) Not reachable at all
                    deadMethods.add(cls + "::" + method);
                } else {
                    // (B) Reachable but purely constant return + no side effects
                    CPValue retVal = cp.getReturnValue(cls, method);
                    if (retVal.isConst() && !cp.methodHasSideEffects(cls, method)) {
                        deadMethods.add(cls + "::" + method);
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Phase 3: per-method liveness + marking
    // ---------------------------------------------------------------

    private void analyseMethod(String cls, String method) {
        List<Node> stmts = getStatementList(cls, method);
        if (stmts == null || stmts.isEmpty()) {
            everLive.put(cls + "::" + method, new LinkedHashSet<>());
            return;
        }

        int n = stmts.size();
        @SuppressWarnings("unchecked")
        Set<String>[] liveOut = new Set[n];
        for (int i = 0; i < n; i++)
            liveOut[i] = new LinkedHashSet<>();

        Set<String> exitLive = new LinkedHashSet<>();
        if (!"main".equals(method)) {
            MethodDeclaration md = lookupMethod(cls, method);
            if (md != null)
                addIfLive(exitLive, md.f10.f0.tokenImage, cls, method);
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            Set<String> live = new LinkedHashSet<>(exitLive);
            for (int i = n - 1; i >= 0; i--) {
                if (!live.equals(liveOut[i])) {
                    liveOut[i] = new LinkedHashSet<>(live);
                    changed = true;
                }
                live = liveIn(unwrap(stmts.get(i)), live, cls, method);
            }
        }

        Set<String> ever = new LinkedHashSet<>(exitLive);
        for (int i = 0; i < n; i++)
            ever.addAll(liveOut[i]);
        if (n > 0)
            ever.addAll(liveIn(unwrap(stmts.get(0)), liveOut[0], cls, method));
        everLive.put(cls + "::" + method, ever);

        for (int i = 0; i < n; i++)
            markDeadStatementRecursive(unwrap(stmts.get(i)), liveOut[i], cls, method);
    }

    // ---------------------------------------------------------------
    // Transfer function
    // ---------------------------------------------------------------

    private Set<String> liveIn(Node inner, Set<String> liveOut,
            String cls, String method) {
        Set<String> live = new LinkedHashSet<>(liveOut);

        if (inner instanceof AssignmentStatement) {
            AssignmentStatement as = (AssignmentStatement) inner;
            live.remove(as.f0.f0.tokenImage);
            live.addAll(usesOfExpression(as.f2, cls, method));

        } else if (inner instanceof ArrayAssignmentStatement) {
            ArrayAssignmentStatement aas = (ArrayAssignmentStatement) inner;
            live.add(aas.f0.f0.tokenImage);
            addIfLive(live, aas.f2.f0.tokenImage, cls, method);
            addIfLive(live, aas.f5.f0.tokenImage, cls, method);

        } else if (inner instanceof FieldAssignmentStatement) {
            FieldAssignmentStatement fas = (FieldAssignmentStatement) inner;
            live.add(fas.f0.f0.tokenImage);
            addIfLive(live, fas.f4.f0.tokenImage, cls, method);

        } else if (inner instanceof PrintStatement) {
            addIfLive(live, ((PrintStatement) inner).f2.f0.tokenImage, cls, method);

        } else if (inner instanceof IfStatement) {
            IfStatement is = (IfStatement) inner;
            String condName = is.f2.f0.tokenImage;
            CPValue condVal = cp.getVarValue(cls, method, condName);
            if (condVal.isTrue()) {
                live = liveIn(unwrap(is.f4), new LinkedHashSet<>(liveOut), cls, method);
            } else if (condVal.isFalse()) {
                live = liveIn(unwrap(is.f6), new LinkedHashSet<>(liveOut), cls, method);
            } else {
                live = union(
                        liveIn(unwrap(is.f4), new LinkedHashSet<>(liveOut), cls, method),
                        liveIn(unwrap(is.f6), new LinkedHashSet<>(liveOut), cls, method));
                addIfLive(live, condName, cls, method);
            }

        } else if (inner instanceof WhileStatement) {
            WhileStatement ws = (WhileStatement) inner;
            String condName = ws.f2.f0.tokenImage;
            addIfLive(live, condName, cls, method);
            if (!cp.getVarValue(cls, method, condName).isFalse())
                live.addAll(liveIn(unwrap(ws.f4), new LinkedHashSet<>(liveOut), cls, method));

        } else if (inner instanceof ForStatement) {
            ForStatement fs = (ForStatement) inner;
            live.addAll(usesOfExpression(fs.f4, cls, method));
            live.addAll(usesOfExpression(fs.f6, cls, method));
            live.addAll(usesOfExpression(fs.f10, cls, method));
            live.remove(fs.f2.f0.tokenImage);
            live.remove(fs.f8.f0.tokenImage);
            live.addAll(liveIn(unwrap(fs.f12), new LinkedHashSet<>(liveOut), cls, method));

        } else if (inner instanceof Block) {
            List<Node> inner2 = nodeListToList(((Block) inner).f1);
            Set<String> cur = new LinkedHashSet<>(liveOut);
            for (int i = inner2.size() - 1; i >= 0; i--)
                cur = liveIn(unwrap(inner2.get(i)), cur, cls, method);
            live = cur;
        }
        return live;
    }

    // ---------------------------------------------------------------
    // Dead-code marking — recursive into nested bodies
    // ---------------------------------------------------------------

    private void markDeadStatementRecursive(Node inner, Set<String> liveOut,
            String cls, String method) {
        if (inner instanceof AssignmentStatement) {
            AssignmentStatement as = (AssignmentStatement) inner;
            if (!liveOut.contains(as.f0.f0.tokenImage) && !hasSideEffectsToKeep(as, cls, method))
                deadAssignments.add(inner);

        } else if (inner instanceof IfStatement) {
            IfStatement is = (IfStatement) inner;
            CPValue cv = cp.getVarValue(cls, method, is.f2.f0.tokenImage);
            if (cv.isTrue())
                deadIfBranch.put(inner, "else");
            if (cv.isFalse())
                deadIfBranch.put(inner, "then");
            if (cv.isTrue() || !cv.isFalse())
                recurseIntoBody(is.f4, liveOut, cls, method);
            if (cv.isFalse() || !cv.isTrue())
                recurseIntoBody(is.f6, liveOut, cls, method);

        } else if (inner instanceof WhileStatement) {
            WhileStatement ws = (WhileStatement) inner;
            CPValue cv = cp.getVarValue(cls, method, ws.f2.f0.tokenImage);
            if (cv.isFalse()) {
                deadWhiles.add(inner);
                return;
            }
            Set<String> bodyLiveOut = new LinkedHashSet<>(liveOut);
            addIfLive(bodyLiveOut, ws.f2.f0.tokenImage, cls, method);
            recurseIntoBody(ws.f4, bodyLiveOut, cls, method);

        } else if (inner instanceof ForStatement) {
            ForStatement fs = (ForStatement) inner;
            Set<String> bodyLiveOut = new LinkedHashSet<>(liveOut);
            bodyLiveOut.addAll(usesOfExpression(fs.f6, cls, method));
            bodyLiveOut.addAll(usesOfExpression(fs.f10, cls, method));
            recurseIntoBody(fs.f12, bodyLiveOut, cls, method);

        } else if (inner instanceof Block) {
            recurseIntoBlock((Block) inner, liveOut, cls, method);
        }
    }

    private void recurseIntoBody(Node bodyNode, Set<String> liveOut,
            String cls, String method) {
        Node inner = unwrap(bodyNode);
        if (inner instanceof Block)
            recurseIntoBlock((Block) inner, liveOut, cls, method);
        else
            markDeadStatementRecursive(inner, liveOut, cls, method);
    }

    private void recurseIntoBlock(Block b, Set<String> blockLiveOut,
            String cls, String method) {
        List<Node> stmts = nodeListToList(b.f1);
        int n = stmts.size();
        if (n == 0)
            return;
        @SuppressWarnings("unchecked")
        Set<String>[] lo = new Set[n];
        Set<String> cur = new LinkedHashSet<>(blockLiveOut);
        for (int i = n - 1; i >= 0; i--) {
            lo[i] = new LinkedHashSet<>(cur);
            cur = liveIn(unwrap(stmts.get(i)), cur, cls, method);
        }
        for (int i = 0; i < n; i++)
            markDeadStatementRecursive(unwrap(stmts.get(i)), lo[i], cls, method);
    }

    // ---------------------------------------------------------------
    // Side-effect check for assignment RHS
    //
    // An assignment x = expr; should be kept if:
    // - expr is a MessageSend AND the callee has real side effects
    //
    // An assignment x = obj.foo(); should be DROPPED (not promoted) if:
    // - callee has no side effects (pure constant return)
    //
    // An assignment x = obj.foo(); should be PROMOTED to void call if:
    // - callee has side effects but x is dead
    // ---------------------------------------------------------------

    private boolean hasSideEffectsToKeep(AssignmentStatement as,
            String cls, String method) {
        if (!(as.f2.f0.choice instanceof MessageSend))
            return false;
        // It's a MessageSend — keep if the callee has side effects
        // (will be promoted to void call in CodeGen)
        // Drop if the callee is pure (constant return, no side effects)
        MessageSend ms = (MessageSend) as.f2.f0.choice;
        String methodName = ms.f2.f0.tokenImage;
        String receiverType = resolveReceiverType(ms, cls, method);
        if (receiverType == null)
            return true; // unknown -> conservative keep

        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(receiverType);
        candidates.addAll(ch.allSubclasses(receiverType));
        for (String candidate : candidates) {
            String dc2 = ch.declaringClass(candidate, methodName);
            if (dc2 != null && cg.reachable(dc2, methodName))
                if (cp.methodHasSideEffects(dc2, methodName))
                    return true;
        }
        return false; // all targets are pure -> safe to drop
    }

    // ---------------------------------------------------------------
    // CP-aware uses of an expression
    // ---------------------------------------------------------------

    private Set<String> usesOfExpression(Expression expr, String cls, String method) {
        Set<String> uses = new LinkedHashSet<>();
        Node choice = expr.f0.choice;

        if (choice instanceof CompareExpression) {
            CompareExpression ce = (CompareExpression) choice;
            addIfLive(uses, ce.f0.f0.tokenImage, cls, method);
            addIfLive(uses, ce.f2.f0.tokenImage, cls, method);
        } else if (choice instanceof PlusExpression) {
            PlusExpression pe = (PlusExpression) choice;
            addIfLive(uses, pe.f0.f0.tokenImage, cls, method);
            addIfLive(uses, pe.f2.f0.tokenImage, cls, method);
        } else if (choice instanceof MinusExpression) {
            MinusExpression me = (MinusExpression) choice;
            addIfLive(uses, me.f0.f0.tokenImage, cls, method);
            addIfLive(uses, me.f2.f0.tokenImage, cls, method);
        } else if (choice instanceof TimesExpression) {
            TimesExpression te = (TimesExpression) choice;
            addIfLive(uses, te.f0.f0.tokenImage, cls, method);
            addIfLive(uses, te.f2.f0.tokenImage, cls, method);
        } else if (choice instanceof ArrayLookup) {
            ArrayLookup al = (ArrayLookup) choice;
            uses.add(al.f0.f0.tokenImage);
            addIfLive(uses, al.f2.f0.tokenImage, cls, method);
        } else if (choice instanceof ArrayLength) {
            uses.add(((ArrayLength) choice).f0.f0.tokenImage);
        } else if (choice instanceof MessageSend) {
            MessageSend ms = (MessageSend) choice;
            // Receiver: always live (object reference)
            Node recv = ms.f0.f0.choice;
            if (recv instanceof Identifier) {
                // Only live if callee is not constant+pure (if it is, the whole
                // call folds to a literal and the receiver object is not needed)
                if (!isCalleeConstantAndPure(ms, cls, method))
                    uses.add(((Identifier) recv).f0.tokenImage);
            }
            // Args: only live if callee is not dead
            if (!isCalleeConstantAndPure(ms, cls, method) && ms.f4.present()) {
                ArgList al = (ArgList) ms.f4.node;
                addIfLive(uses, al.f0.f0.tokenImage, cls, method);
                if (al.f1.present())
                    for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();)
                        addIfLive(uses, ((ArgRest) e.nextElement()).f1.f0.tokenImage, cls, method);
            }
        } else if (choice instanceof FieldRead) {
            uses.add(((FieldRead) choice).f0.f0.tokenImage);
        } else if (choice instanceof PrimaryExpression) {
            Node prim = ((PrimaryExpression) choice).f0.choice;
            if (prim instanceof Identifier)
                addIfLive(uses, ((Identifier) prim).f0.tokenImage, cls, method);
            else if (prim instanceof NotExpression)
                addIfLive(uses, ((NotExpression) prim).f1.f0.tokenImage, cls, method);
            else if (prim instanceof ArrayAllocationExpression)
                addIfLive(uses, ((ArrayAllocationExpression) prim).f3.f0.tokenImage, cls, method);
        }
        return uses;
    }

    /**
     * Returns true if all CHA targets of this MessageSend are
     * constant-returning and side-effect-free (i.e., the whole call
     * will be CP-substituted and the callee eliminated).
     */
    private boolean isCalleeConstantAndPure(MessageSend ms, String cls, String method) {
        String methodName = ms.f2.f0.tokenImage;
        String receiverType = resolveReceiverType(ms, cls, method);
        if (receiverType == null)
            return false;

        CPValue ret = cp.getReturnValue(receiverType, methodName);
        if (!ret.isConst())
            return false;

        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(receiverType);
        candidates.addAll(ch.allSubclasses(receiverType));
        for (String candidate : candidates) {
            String dc2 = ch.declaringClass(candidate, methodName);
            if (dc2 != null && cg.reachable(dc2, methodName))
                if (cp.methodHasSideEffects(dc2, methodName))
                    return false;
        }
        return true;
    }

    private void addIfLive(Set<String> liveSet, String varName, String cls, String method) {
        CPValue val = cp.getIdValue(cls, method, varName);
        if (!val.isConst()) {
            liveSet.add(varName);
        }
    }

    private boolean hasSideEffects(Expression expr) {
        return expr.f0.choice instanceof MessageSend;
    }

    // ---------------------------------------------------------------
    // Receiver type resolution (for use outside CP context)
    // ---------------------------------------------------------------

    private String resolveReceiverType(MessageSend ms, String cls, String method) {
        Node choice = ms.f0.f0.choice;
        if (choice instanceof Identifier)
            return st.typeOf(cls, method, ((Identifier) choice).f0.tokenImage);
        if (choice instanceof ThisExpression)
            return cls;
        if (choice instanceof AllocationExpression)
            return ((AllocationExpression) choice).f1.f0.tokenImage;
        return null;
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private static Node unwrap(Node n) {
        if (n instanceof Statement)
            return ((Statement) n).f0.choice;
        return n;
    }

    private List<Node> getStatementList(String cls, String method) {
        NodeListOptional nlo;
        if ("main".equals(method) && mainClassNode != null)
            nlo = mainClassNode.f15;
        else {
            MethodDeclaration md = lookupMethod(cls, method);
            if (md == null)
                return null;
            nlo = md.f8;
        }
        return nodeListToList(nlo);
    }

    private List<Node> nodeListToList(NodeListOptional nlo) {
        List<Node> list = new ArrayList<>();
        if (nlo.present())
            for (Enumeration<Node> e = nlo.elements(); e.hasMoreElements();)
                list.add(e.nextElement());
        return list;
    }

    private MethodDeclaration lookupMethod(String cls, String method) {
        String cur = cls;
        while (cur != null) {
            Map<String, MethodDeclaration> m = methodIndex.get(cur);
            if (m != null && m.containsKey(method))
                return m.get(method);
            cur = ch.parentOf(cur);
        }
        return null;
    }

    private static Set<String> union(Set<String> a, Set<String> b) {
        Set<String> r = new LinkedHashSet<>(a);
        r.addAll(b);
        return r;
    }
}