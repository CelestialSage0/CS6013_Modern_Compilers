package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 5: Liveness Analysis + Dead Code Marking.
 *
 * Three fixes over previous version:
 *
 * FIX 1: markDeadStatement now recurses into if/while/block bodies
 * so assignments inside nested structures are also marked dead.
 *
 * FIX 2: Tracks "ever-live" variables — the union of all liveIn sets
 * across every program point in the method. A variable that is
 * never in any liveIn set is never read as a non-constant, so
 * its VarDeclaration can be dropped from the output.
 *
 * FIX 3: CP-aware liveness — identifiers that CP will substitute with
 * a constant are NOT counted as real uses (addIfLive).
 *
 * Dead-node sets are keyed on INNER concrete nodes (unwrapped from Statement).
 * getDeadBranch convention:
 * "else" -> else-branch dead (condition true -> keep then)
 * "then" -> then-branch dead (condition false -> keep else)
 */
public class DeadCodeVisitor extends GJDepthFirst {

    private final ClassHierarchy ch;
    private final SymbolTable st;
    private final CallGraph cg;
    private final ConstPropVisitor cp;

    // ---------------------------------------------------------------
    // Results — keyed on INNER concrete nodes
    // ---------------------------------------------------------------
    private final Set<Node> deadAssignments = Collections.newSetFromMap(
            new IdentityHashMap<>());
    private final Map<Node, String> deadIfBranch = new IdentityHashMap<>();
    private final Set<Node> deadWhiles = Collections.newSetFromMap(
            new IdentityHashMap<>());
    private final Set<String> deadMethods = new LinkedHashSet<>();

    /**
     * Per-method set of variables that are EVER live at any program point.
     * "cls::method" -> set of variable names that are truly needed.
     * Variables NOT in this set can have their VarDeclaration dropped.
     */
    private final Map<String, Set<String>> everLive = new LinkedHashMap<>();

    // ---------------------------------------------------------------
    // Method AST index
    // ---------------------------------------------------------------
    private final Map<String, Map<String, MethodDeclaration>> methodIndex = new LinkedHashMap<>();
    private MainClass mainClassNode;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------
    public DeadCodeVisitor(ClassHierarchy ch,
            SymbolTable st,
            CallGraph cg,
            ConstPropVisitor cp) {
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

    /**
     * Should the VarDeclaration for varName in (cls, method) be dropped?
     * True iff the variable is never live at any program point
     * (i.e., it is always substituted by a constant or never used).
     */
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
    // Phase 2: dead methods
    // ---------------------------------------------------------------

    private void markDeadMethods() {
        for (String cls : ch.classNames())
            for (String m : ch.methodsOf(cls))
                if (!cg.reachable(cls, m))
                    deadMethods.add(cls + "::" + m);
    }

    // ---------------------------------------------------------------
    // Phase 3: per-method backward liveness + marking
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

        // Exit live: return variable (skipped if CP substitutes it)
        Set<String> exitLive = new LinkedHashSet<>();
        if (!"main".equals(method)) {
            MethodDeclaration md = lookupMethod(cls, method);
            if (md != null)
                addIfLive(exitLive, md.f10.f0.tokenImage, cls, method);
        }

        // Backward fixpoint over the flat statement list
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

        // Accumulate ever-live: union of all liveOut sets + liveIn of stmt[0]
        Set<String> ever = new LinkedHashSet<>(exitLive);
        for (int i = 0; i < n; i++)
            ever.addAll(liveOut[i]);
        // Also include liveIn of the first statement (= variables live on entry)
        if (n > 0)
            ever.addAll(liveIn(unwrap(stmts.get(0)), liveOut[0], cls, method));
        everLive.put(cls + "::" + method, ever);

        // Mark dead statements — recursively into nested bodies
        for (int i = 0; i < n; i++)
            markDeadStatementRecursive(unwrap(stmts.get(i)), liveOut[i], cls, method);
    }

    // ---------------------------------------------------------------
    // Transfer function: liveIn = (liveOut - defs) ∪ effectiveUses
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
                Set<String> thenLive = liveIn(unwrap(is.f4),
                        new LinkedHashSet<>(liveOut), cls, method);
                Set<String> elseLive = liveIn(unwrap(is.f6),
                        new LinkedHashSet<>(liveOut), cls, method);
                live = union(thenLive, elseLive);
                addIfLive(live, condName, cls, method);
            }

        } else if (inner instanceof WhileStatement) {
            WhileStatement ws = (WhileStatement) inner;
            String condName = ws.f2.f0.tokenImage;
            CPValue condVal = cp.getVarValue(cls, method, condName);
            addIfLive(live, condName, cls, method);
            if (!condVal.isFalse()) {
                Set<String> bodyLive = liveIn(unwrap(ws.f4),
                        new LinkedHashSet<>(liveOut), cls, method);
                live.addAll(bodyLive);
            }

        } else if (inner instanceof ForStatement) {
            ForStatement fs = (ForStatement) inner;
            live.addAll(usesOfExpression(fs.f4, cls, method));
            live.addAll(usesOfExpression(fs.f6, cls, method));
            live.addAll(usesOfExpression(fs.f10, cls, method));
            live.remove(fs.f2.f0.tokenImage);
            live.remove(fs.f8.f0.tokenImage);
            Set<String> bodyLive = liveIn(unwrap(fs.f12),
                    new LinkedHashSet<>(liveOut), cls, method);
            live.addAll(bodyLive);

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
    // Dead-code marking — RECURSIVE into nested bodies
    //
    // Previous version only ran markDeadStatement on the flat top-level
    // list. This meant assignments inside if/while/block bodies were
    // never examined. Now we recurse so every AssignmentStatement in
    // the entire method body gets a chance to be marked dead.
    //
    // For each nested statement we recompute liveOut at that point:
    // - For a Block: backward scan gives liveOut for each element
    // - For an IfStatement branch: the liveOut of the branch is the
    // liveOut of the whole if-statement (both branches exit to same point)
    // - For a WhileStatement body: liveOut of body = liveIn of condition
    // (conservatively the liveOut of the while, since loop may repeat)
    // ---------------------------------------------------------------

    private void markDeadStatementRecursive(Node inner, Set<String> liveOut,
            String cls, String method) {
        if (inner instanceof AssignmentStatement) {
            AssignmentStatement as = (AssignmentStatement) inner;
            if (!liveOut.contains(as.f0.f0.tokenImage) && !hasSideEffects(as.f2))
                deadAssignments.add(inner);

        } else if (inner instanceof IfStatement) {
            IfStatement is = (IfStatement) inner;
            CPValue cv = cp.getVarValue(cls, method, is.f2.f0.tokenImage);
            if (cv.isTrue())
                deadIfBranch.put(inner, "else");
            if (cv.isFalse())
                deadIfBranch.put(inner, "then");

            // Recurse into branches:
            // liveOut for either branch body = liveOut of the whole if-statement
            // (both branches flow to the same successor)
            if (cv.isTrue() || !cv.isFalse()) {
                // then-branch may execute
                recurseIntoBody(is.f4, liveOut, cls, method);
            }
            if (cv.isFalse() || !cv.isTrue()) {
                // else-branch may execute
                recurseIntoBody(is.f6, liveOut, cls, method);
            }

        } else if (inner instanceof WhileStatement) {
            WhileStatement ws = (WhileStatement) inner;
            CPValue cv = cp.getVarValue(cls, method, ws.f2.f0.tokenImage);
            if (cv.isFalse()) {
                deadWhiles.add(inner);
                return; // body unreachable — nothing to recurse into
            }
            // liveOut for the body = liveIn of the while (conservative:
            // after the body, we re-evaluate the condition and may loop)
            // The safest liveOut for the body is: liveOut of while ∪ uses(cond)
            Set<String> bodyLiveOut = new LinkedHashSet<>(liveOut);
            addIfLive(bodyLiveOut, ws.f2.f0.tokenImage, cls, method);
            recurseIntoBody(ws.f4, bodyLiveOut, cls, method);

        } else if (inner instanceof ForStatement) {
            ForStatement fs = (ForStatement) inner;
            // liveOut for body = liveOut of for ∪ uses(stepExpr) ∪ uses(condExpr)
            Set<String> bodyLiveOut = new LinkedHashSet<>(liveOut);
            bodyLiveOut.addAll(usesOfExpression(fs.f6, cls, method));
            bodyLiveOut.addAll(usesOfExpression(fs.f10, cls, method));
            recurseIntoBody(fs.f12, bodyLiveOut, cls, method);

        } else if (inner instanceof Block) {
            recurseIntoBlock((Block) inner, liveOut, cls, method);
        }
        // AssignmentStatement handled above.
        // PrintStatement, ArrayAssign, FieldAssign: always live, no sub-statements.
    }

    /**
     * Recurse into a Statement or Block body with the given liveOut.
     */
    private void recurseIntoBody(Node bodyNode, Set<String> liveOut,
            String cls, String method) {
        Node inner = unwrap(bodyNode);
        if (inner instanceof Block) {
            recurseIntoBlock((Block) inner, liveOut, cls, method);
        } else {
            markDeadStatementRecursive(inner, liveOut, cls, method);
        }
    }

    /**
     * Backward scan through a block's statement list to compute per-statement
     * liveOut, then mark each statement dead if appropriate.
     */
    private void recurseIntoBlock(Block b, Set<String> blockLiveOut,
            String cls, String method) {
        List<Node> stmts = nodeListToList(b.f1);
        int n = stmts.size();
        if (n == 0)
            return;

        // Compute liveOut for each statement by backward scan
        @SuppressWarnings("unchecked")
        Set<String>[] lo = new Set[n];
        Set<String> cur = new LinkedHashSet<>(blockLiveOut);
        for (int i = n - 1; i >= 0; i--) {
            lo[i] = new LinkedHashSet<>(cur);
            cur = liveIn(unwrap(stmts.get(i)), cur, cls, method);
        }

        // Recursively mark each statement
        for (int i = 0; i < n; i++)
            markDeadStatementRecursive(unwrap(stmts.get(i)), lo[i], cls, method);
    }

    // ---------------------------------------------------------------
    // Side-effect check
    // ---------------------------------------------------------------

    private boolean hasSideEffects(Expression expr) {
        return expr.f0.choice instanceof MessageSend;
    }

    // ---------------------------------------------------------------
    // CP-aware uses of an expression
    // ---------------------------------------------------------------

    private Set<String> usesOfExpression(Expression expr,
            String cls, String method) {
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
            Node recv = ms.f0.f0.choice;
            if (recv instanceof Identifier)
                uses.add(((Identifier) recv).f0.tokenImage);
            if (ms.f4.present()) {
                ArgList al = (ArgList) ms.f4.node;
                addIfLive(uses, al.f0.f0.tokenImage, cls, method);
                if (al.f1.present()) {
                    for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();) {
                        ArgRest ar = (ArgRest) e.nextElement();
                        addIfLive(uses, ar.f1.f0.tokenImage, cls, method);
                    }
                }
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
                addIfLive(uses, ((ArrayAllocationExpression) prim).f3.f0.tokenImage,
                        cls, method);
        }
        return uses;
    }

    // ---------------------------------------------------------------
    // addIfLive: only add to live set if CP won't substitute this var
    // ---------------------------------------------------------------

    private void addIfLive(Set<String> liveSet, String varName,
            String cls, String method) {
        if (!cp.getIdValue(cls, method, varName).isConst())
            liveSet.add(varName);
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
        if ("main".equals(method) && mainClassNode != null) {
            nlo = mainClassNode.f15;
        } else {
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