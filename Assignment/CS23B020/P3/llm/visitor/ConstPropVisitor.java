package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 4: Intraprocedural + Interprocedural Constant Propagation.
 *
 * Key addition: evalExpression now folds MessageSend calls whose callee
 * always returns a known constant (via getReturnValue). This propagates
 * constants from callees back into callers.
 *
 * Also provides:
 * getReturnValue(cls, method) - CPValue of the return expression
 * methodHasSideEffects(cls, method) - true if body contains println
 * or field writes
 */
public class ConstPropVisitor extends GJDepthFirst {

    private final ClassHierarchy ch;
    private final SymbolTable st;
    private final CallGraph cg;

    // className -> methodName -> varName -> CPValue (at fixpoint)
    private final Map<String, Map<String, Map<String, CPValue>>> methodStates = new LinkedHashMap<>();

    // Interprocedural param summaries: "cls::method" -> param -> CPValue
    private final Map<String, Map<String, CPValue>> paramSummaries = new LinkedHashMap<>();

    // Method AST index
    private final Map<String, Map<String, MethodDeclaration>> methodIndex = new LinkedHashMap<>();
    private MainClass mainClassNode;

    // Mutable context during analysis of one method
    private String currentClass;
    private String currentMethod;
    private Map<String, CPValue> state;

    public ConstPropVisitor(ClassHierarchy ch,
            SymbolTable st,
            CallGraph cg) {
        this.ch = ch;
        this.st = st;
        this.cg = cg;
    }

    // ---------------------------------------------------------------
    // Public query API
    // ---------------------------------------------------------------

    public CPValue getVarValue(String cls, String method, String varName) {
        Map<String, Map<String, CPValue>> m1 = methodStates.get(cls);
        if (m1 == null)
            return CPValue.NAC;
        Map<String, CPValue> m2 = m1.get(method);
        if (m2 == null)
            return CPValue.NAC;
        CPValue v = m2.get(varName);
        return v == null ? CPValue.UNDEF : v;
    }

    public CPValue getIdValue(String cls, String method, String name) {
        String cur = cls;
        while (cur != null) {
            if (st.fieldsOf(cur).containsKey(name))
                return CPValue.NAC;
            cur = ch.parentOf(cur);
        }
        return getVarValue(cls, method, name);
    }

    /**
     * CPValue of what method (cls, method) returns.
     * Looks up the return variable (f10) in that method's final CP state.
     * If any CHA override returns a different constant -> NAC.
     * Returns NAC if not all targets return the same constant.
     */
    public CPValue getReturnValue(String receiverType, String methodName) {
        // CHA: all possible targets
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(receiverType);
        candidates.addAll(ch.allSubclasses(receiverType));

        CPValue result = CPValue.UNDEF;
        for (String candidate : candidates) {
            String declaringCls = ch.declaringClass(candidate, methodName);
            if (declaringCls == null)
                continue;
            if (!cg.reachable(declaringCls, methodName))
                continue;

            MethodDeclaration md = lookupMethod(declaringCls, methodName);
            if (md == null) {
                result = CPValue.NAC;
                break;
            }

            String retVar = md.f10.f0.tokenImage;
            CPValue retVal = getVarValue(declaringCls, methodName, retVar);
            result = CPValue.meet(result, retVal);
            if (result.isNAC())
                break;
        }
        return result.isUndef() ? CPValue.NAC : result;
    }

    /**
     * Returns true if the method body contains any statement with an
     * observable side effect other than its return value:
     * - System.out.println
     * - FieldAssignmentStatement
     * Used to decide if a method can be eliminated after CP.
     */
    public boolean methodHasSideEffects(String cls, String method) {
        MethodDeclaration md = lookupMethod(cls, method);
        if (md == null)
            return false;
        SideEffectChecker checker = new SideEffectChecker();
        md.f8.accept(checker, null);
        return checker.found;
    }

    // ---------------------------------------------------------------
    // Interprocedural API
    // ---------------------------------------------------------------

    public void setParamSummary(String cls, String method,
            String param, CPValue value) {
        paramSummaries.computeIfAbsent(cls + "::" + method,
                k -> new LinkedHashMap<>())
                .put(param, value);
    }

    public CPValue getParamSummary(String cls, String method, String param) {
        Map<String, CPValue> m = paramSummaries.get(cls + "::" + method);
        return m == null ? CPValue.NAC : m.getOrDefault(param, CPValue.NAC);
    }

    /**
     * Re-run analysis of all reachable methods with current param summaries.
     * Returns true if any method state changed.
     */
    public boolean reAnalyse() {
        Map<String, Map<String, Map<String, CPValue>>> old = deepCopy(methodStates);
        methodStates.clear();
        for (String key : cg.reachableSet()) {
            String[] p = key.split("::");
            analyseMethod(p[0], p[1]);
        }
        return !methodStates.equals(old);
    }

    public CPValue lookupVarInMethod(String cls, String method, String name) {
        return getIdValue(cls, method, name);
    }

    // ---------------------------------------------------------------
    // Entry point via Goal
    // ---------------------------------------------------------------

    @Override
    public Object visit(Goal n, Object argu) {
        n.f0.accept(this, "INDEX");
        n.f1.accept(this, "INDEX");
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
    // Phase 2: per-method analysis
    // ---------------------------------------------------------------

    private void analyseMethod(String cls, String method) {
        currentClass = cls;
        currentMethod = method;
        state = new LinkedHashMap<>();

        if ("main".equals(method)) {
            for (String local : st.localsOf(cls, method).keySet())
                state.put(local, CPValue.UNDEF);
            runFixpoint(mainClassNode.f15);
        } else {
            MethodDeclaration md = lookupMethod(cls, method);
            if (md == null) {
                currentClass = null;
                currentMethod = null;
                return;
            }

            for (String param : st.paramsOf(cls, method))
                state.put(param, getParamSummary(cls, method, param));
            for (String local : st.localsOf(cls, method).keySet())
                if (!state.containsKey(local))
                    state.put(local, CPValue.UNDEF);

            runFixpoint(md.f8);
        }

        methodStates.computeIfAbsent(cls, k -> new LinkedHashMap<>())
                .put(method, new LinkedHashMap<>(state));
        currentClass = null;
        currentMethod = null;
        state = null;
    }

    private void runFixpoint(NodeListOptional stmts) {
        boolean changed = true;
        while (changed) {
            Map<String, CPValue> before = new LinkedHashMap<>(state);
            runStatements(stmts);
            changed = !state.equals(before);
        }
    }

    private void runStatements(NodeListOptional stmts) {
        if (!stmts.present())
            return;
        for (Enumeration<Node> e = stmts.elements(); e.hasMoreElements();)
            e.nextElement().accept(this, null);
    }

    // ---------------------------------------------------------------
    // Statement visitors
    // ---------------------------------------------------------------

    @Override
    public Object visit(AssignmentStatement n, Object argu) {
        String lhs = n.f0.f0.tokenImage;
        if (!isField(lhs))
            state.put(lhs, evalExpression(n.f2));
        return null;
    }

    @Override
    public Object visit(ArrayAssignmentStatement n, Object argu) {
        return null;
    }

    @Override
    public Object visit(FieldAssignmentStatement n, Object argu) {
        return null;
    }

    @Override
    public Object visit(PrintStatement n, Object argu) {
        return null;
    }

    @Override
    public Object visit(Block n, Object argu) {
        runStatements(n.f1);
        return null;
    }

    @Override
    public Object visit(IfStatement n, Object argu) {
        CPValue condVal = lookupVar(n.f2.f0.tokenImage);
        if (condVal.isTrue()) {
            n.f4.accept(this, argu);
        } else if (condVal.isFalse()) {
            n.f6.accept(this, argu);
        } else {
            Map<String, CPValue> snap = new LinkedHashMap<>(state);
            n.f4.accept(this, argu);
            Map<String, CPValue> afterThen = new LinkedHashMap<>(state);
            state = new LinkedHashMap<>(snap);
            n.f6.accept(this, argu);
            state = meetMaps(afterThen, state);
        }
        return null;
    }

    @Override
    public Object visit(WhileStatement n, Object argu) {
        if (lookupVar(n.f2.f0.tokenImage).isFalse())
            return null;
        Map<String, CPValue> preLoop = new LinkedHashMap<>(state);
        boolean changed = true;
        while (changed) {
            Map<String, CPValue> before = new LinkedHashMap<>(state);
            n.f4.accept(this, argu);
            changed = !state.equals(before);
        }
        state = meetMaps(preLoop, state);
        return null;
    }

    @Override
    public Object visit(ForStatement n, Object argu) {
        String initVar = n.f2.f0.tokenImage;
        if (!isField(initVar))
            state.put(initVar, evalExpression(n.f4));
        if (evalExpression(n.f6).isFalse())
            return null;
        Map<String, CPValue> preLoop = new LinkedHashMap<>(state);
        boolean changed = true;
        while (changed) {
            Map<String, CPValue> before = new LinkedHashMap<>(state);
            n.f12.accept(this, argu);
            String stepVar = n.f8.f0.tokenImage;
            if (!isField(stepVar))
                state.put(stepVar, evalExpression(n.f10));
            changed = !state.equals(before);
        }
        state = meetMaps(preLoop, state);
        return null;
    }

    // ---------------------------------------------------------------
    // Expression evaluator
    // ---------------------------------------------------------------

    private CPValue evalExpression(Expression n) {
        Node choice = n.f0.choice;
        if (choice instanceof CompareExpression) {
            CompareExpression ce = (CompareExpression) choice;
            CPValue l = lookupVar(ce.f0.f0.tokenImage), r = lookupVar(ce.f2.f0.tokenImage);
            return (l.isInt() && r.isInt()) ? CPValue.ofBool(l.intValue() < r.intValue()) : CPValue.NAC;
        } else if (choice instanceof PlusExpression) {
            PlusExpression pe = (PlusExpression) choice;
            CPValue l = lookupVar(pe.f0.f0.tokenImage), r = lookupVar(pe.f2.f0.tokenImage);
            return (l.isInt() && r.isInt()) ? CPValue.ofInt(l.intValue() + r.intValue()) : CPValue.NAC;
        } else if (choice instanceof MinusExpression) {
            MinusExpression me = (MinusExpression) choice;
            CPValue l = lookupVar(me.f0.f0.tokenImage), r = lookupVar(me.f2.f0.tokenImage);
            return (l.isInt() && r.isInt()) ? CPValue.ofInt(l.intValue() - r.intValue()) : CPValue.NAC;
        } else if (choice instanceof TimesExpression) {
            TimesExpression te = (TimesExpression) choice;
            CPValue l = lookupVar(te.f0.f0.tokenImage), r = lookupVar(te.f2.f0.tokenImage);
            return (l.isInt() && r.isInt()) ? CPValue.ofInt(l.intValue() * r.intValue()) : CPValue.NAC;
        } else if (choice instanceof PrimaryExpression) {
            return evalPrimary((PrimaryExpression) choice);
        } else if (choice instanceof MessageSend) {
            // --- KEY FIX: fold call if callee always returns a constant ---
            return evalMessageSend((MessageSend) choice);
        }
        return CPValue.NAC;
    }

    /**
     * Evaluate a MessageSend by looking up the callee's return value.
     *
     * Resolution via CHA: receiver's declared type + all subclasses.
     * If ALL reachable CHA targets return the same constant -> return it.
     * Otherwise -> NAC.
     *
     * Note: we use the CURRENT methodStates (which may be partial during
     * the first analysis pass). The interprocedural fixpoint loop in
     * InterConstPropVisitor ensures we converge to the correct answer.
     */
    private CPValue evalMessageSend(MessageSend ms) {
        String methodName = ms.f2.f0.tokenImage;

        // Resolve receiver type from current context
        String receiverType = resolveReceiverType(ms);
        if (receiverType == null)
            return CPValue.NAC;

        return getReturnValue(receiverType, methodName);
    }

    /**
     * Resolve the declared static type of the receiver of a MessageSend,
     * using the CURRENT method context (currentClass, currentMethod).
     */
    private String resolveReceiverType(MessageSend ms) {
        Node choice = ms.f0.f0.choice;
        if (choice instanceof Identifier)
            return st.typeOf(currentClass, currentMethod,
                    ((Identifier) choice).f0.tokenImage);
        if (choice instanceof ThisExpression)
            return currentClass;
        if (choice instanceof AllocationExpression)
            return ((AllocationExpression) choice).f1.f0.tokenImage;
        return null;
    }

    private CPValue evalPrimary(PrimaryExpression n) {
        Node choice = n.f0.choice;
        if (choice instanceof IntegerLiteral)
            return CPValue.ofInt(Integer.parseInt(((IntegerLiteral) choice).f0.tokenImage));
        if (choice instanceof TrueLiteral)
            return CPValue.TRUE_VAL;
        if (choice instanceof FalseLiteral)
            return CPValue.FALSE_VAL;
        if (choice instanceof Identifier)
            return lookupVar(((Identifier) choice).f0.tokenImage);
        if (choice instanceof NotExpression) {
            CPValue v = lookupVar(((NotExpression) choice).f1.f0.tokenImage);
            return v.isBool() ? CPValue.ofBool(!v.boolValue()) : CPValue.NAC;
        }
        return CPValue.NAC;
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    CPValue lookupVar(String name) {
        if (isField(name))
            return CPValue.NAC;
        CPValue v = state.get(name);
        return v == null ? CPValue.UNDEF : v;
    }

    boolean isField(String name) {
        String cur = currentClass;
        while (cur != null) {
            if (st.fieldsOf(cur).containsKey(name))
                return true;
            cur = ch.parentOf(cur);
        }
        return false;
    }

    MethodDeclaration lookupMethod(String cls, String method) {
        String cur = cls;
        while (cur != null) {
            Map<String, MethodDeclaration> m = methodIndex.get(cur);
            if (m != null && m.containsKey(method))
                return m.get(method);
            cur = ch.parentOf(cur);
        }
        return null;
    }

    private static Map<String, CPValue> meetMaps(Map<String, CPValue> a,
            Map<String, CPValue> b) {
        Map<String, CPValue> result = new LinkedHashMap<>();
        Set<String> keys = new LinkedHashSet<>(a.keySet());
        keys.addAll(b.keySet());
        for (String k : keys)
            result.put(k, CPValue.meet(
                    a.getOrDefault(k, CPValue.UNDEF),
                    b.getOrDefault(k, CPValue.UNDEF)));
        return result;
    }

    private static Map<String, Map<String, Map<String, CPValue>>> deepCopy(
            Map<String, Map<String, Map<String, CPValue>>> src) {
        Map<String, Map<String, Map<String, CPValue>>> copy = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, Map<String, CPValue>>> e1 : src.entrySet()) {
            Map<String, Map<String, CPValue>> inner = new LinkedHashMap<>();
            for (Map.Entry<String, Map<String, CPValue>> e2 : e1.getValue().entrySet())
                inner.put(e2.getKey(), new LinkedHashMap<>(e2.getValue()));
            copy.put(e1.getKey(), inner);
        }
        return copy;
    }

    // Package-private for InterConstPropVisitor
    Map<String, Map<String, MethodDeclaration>> methodIndex() {
        return methodIndex;
    }

    MainClass mainClassNode() {
        return mainClassNode;
    }

    // ---------------------------------------------------------------
    // Side-effect checker: visits a method body looking for println
    // or field assignments.
    // ---------------------------------------------------------------
    private static class SideEffectChecker extends GJDepthFirst {
        boolean found = false;

        @Override
        public Object visit(PrintStatement n, Object argu) {
            found = true;
            return null;
        }

        @Override
        public Object visit(FieldAssignmentStatement n, Object argu) {
            found = true;
            return null;
        }
        // Stop early once found (visitor still traverses but found=true short-circuits)
    }
}