package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 4: Intraprocedural Conditional Constant Propagation.
 *
 * CRITICAL DESIGN: straight-line assignments OVERWRITE state, they do NOT meet.
 * Meet only happens at control-flow join points (after if/while merges).
 *
 * Fixpoint iteration is needed only for loops — the outer loop re-runs
 * statements until state stabilises. On each iteration, we re-execute
 * assignments with the current state, so later assignments naturally see
 * updated values from earlier ones in the same pass.
 *
 * Per-variable state initialisation:
 * Parameters -> NAC (context-insensitive: unknown caller values)
 * Locals -> UNDEF (not yet assigned on any path)
 *
 * Expressions that are always NAC:
 * MessageSend, FieldRead, ArrayLookup, ArrayLength, new, this
 */
public class ConstPropVisitor extends GJDepthFirst {

    private final ClassHierarchy ch;
    private final SymbolTable st;
    private final CallGraph cg;

    // ---------------------------------------------------------------
    // Results: per-method variable states at fixpoint
    // className -> methodName -> varName -> CPValue
    // ---------------------------------------------------------------
    private final Map<String, Map<String, Map<String, CPValue>>> methodStates = new LinkedHashMap<>();

    // ---------------------------------------------------------------
    // Index of method AST nodes (populated in Phase 1)
    // ---------------------------------------------------------------
    private final Map<String, Map<String, MethodDeclaration>> methodIndex = new LinkedHashMap<>();
    private MainClass mainClassNode;

    // ---------------------------------------------------------------
    // Mutable context during analysis of one method
    // ---------------------------------------------------------------
    private String currentClass;
    private String currentMethod;
    private Map<String, CPValue> state; // current variable -> CPValue map

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------
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

    /**
     * CPValue of varName at the fixpoint of (cls, method).
     * Returns UNDEF if never analysed, NAC if unknown.
     */
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

    /**
     * CPValue for an identifier used inside (cls, method).
     * Fields are always NAC (aliasing). Locals/params use the state map.
     */
    public CPValue getIdValue(String cls, String method, String name) {
        String cur = cls;
        while (cur != null) {
            if (st.fieldsOf(cur).containsKey(name))
                return CPValue.NAC;
            cur = ch.parentOf(cur);
        }
        return getVarValue(cls, method, name);
    }

    // ---------------------------------------------------------------
    // Phase 1 + Phase 2 entry via Goal
    // ---------------------------------------------------------------

    @Override
    public Object visit(Goal n, Object argu) {
        // Phase 1: index all method AST nodes
        n.f0.accept(this, "INDEX");
        n.f1.accept(this, "INDEX");

        // Phase 2: analyse each reachable method
        for (String reachKey : cg.reachableSet()) {
            String[] parts = reachKey.split("::");
            String cls = parts[0];
            String method = parts[1];
            analyseMethod(cls, method);
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
        String cls = (String) argu;
        String method = n.f2.f0.tokenImage;
        methodIndex.computeIfAbsent(cls, k -> new LinkedHashMap<>())
                .put(method, n);
        return null;
    }

    // ---------------------------------------------------------------
    // Phase 2: per-method fixpoint analysis
    // ---------------------------------------------------------------

    private void analyseMethod(String cls, String method) {
        currentClass = cls;
        currentMethod = method;

        // Build initial state
        state = new LinkedHashMap<>();

        if ("main".equals(method)) {
            // main: no parameters, all locals start UNDEF
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

            // Parameters -> NAC (context-insensitive)
            for (String param : st.paramsOf(cls, method))
                state.put(param, CPValue.NAC);
            // Locals -> UNDEF (only if not already set by params)
            for (String local : st.localsOf(cls, method).keySet())
                if (!state.containsKey(local))
                    state.put(local, CPValue.UNDEF);

            runFixpoint(md.f8);
        }

        // Persist fixpoint state
        methodStates.computeIfAbsent(cls, k -> new LinkedHashMap<>())
                .put(method, new LinkedHashMap<>(state));

        currentClass = null;
        currentMethod = null;
        state = null;
    }

    /**
     * Runs statements repeatedly until state stops changing.
     * Needed for loops: the body of a while/for may update variables
     * that are used in the condition or earlier statements.
     *
     * For straight-line code this converges in exactly 1 iteration.
     */
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

    /**
     * AssignmentStatement: x = expr
     * OVERWRITE state[x] with the evaluated value of expr.
     * Do NOT meet — this is a definition, not a join point.
     *
     * Exception: if this statement is inside a loop body that is
     * visited multiple times, we meet at the start of each outer
     * iteration (handled by runFixpoint comparing before/after).
     */
    @Override
    public Object visit(AssignmentStatement n, Object argu) {
        String lhs = n.f0.f0.tokenImage;
        CPValue rhs = evalExpression(n.f2);

        if (!isField(lhs))
            state.put(lhs, rhs); // OVERWRITE — straight-line assignment
        return null;
    }

    @Override
    public Object visit(ArrayAssignmentStatement n, Object argu) {
        return null; // array contents not tracked
    }

    @Override
    public Object visit(FieldAssignmentStatement n, Object argu) {
        return null; // fields always NAC
    }

    /**
     * if (cond) S1 else S2
     *
     * Conditional CP: if cond is a known constant, analyse only the
     * live branch (no join needed). Otherwise analyse both branches
     * on separate state copies and meet at the join point.
     */
    @Override
    public Object visit(IfStatement n, Object argu) {
        String condName = n.f2.f0.tokenImage;
        CPValue condVal = lookupVar(condName);

        if (condVal.isTrue()) {
            n.f4.accept(this, argu); // only then-branch
        } else if (condVal.isFalse()) {
            n.f6.accept(this, argu); // only else-branch
        } else {
            // Both branches — snapshot before, analyse each, meet after
            Map<String, CPValue> snap = new LinkedHashMap<>(state);

            n.f4.accept(this, argu);
            Map<String, CPValue> afterThen = new LinkedHashMap<>(state);

            state = new LinkedHashMap<>(snap);
            n.f6.accept(this, argu);
            Map<String, CPValue> afterElse = new LinkedHashMap<>(state);

            state = meetMaps(afterThen, afterElse);
        }
        return null;
    }

    /**
     * while (cond) body
     *
     * If cond is statically false, skip body.
     * Otherwise iterate body to fixpoint (inner loop), then meet
     * the pre-loop state with the post-loop state (conservative: loop
     * may execute 0 or more times).
     */
    @Override
    public Object visit(WhileStatement n, Object argu) {
        String condName = n.f2.f0.tokenImage;
        CPValue condVal = lookupVar(condName);

        if (condVal.isFalse())
            return null; // body unreachable

        // State before the loop
        Map<String, CPValue> preLoop = new LinkedHashMap<>(state);

        // Iterate body until state stabilises
        boolean changed = true;
        while (changed) {
            Map<String, CPValue> before = new LinkedHashMap<>(state);
            n.f4.accept(this, argu);
            changed = !state.equals(before);
        }

        // Conservative join: loop may execute 0 times
        state = meetMaps(preLoop, state);
        return null;
    }

    /**
     * for (initVar = initExpr; condExpr; stepVar = stepExpr) body
     *
     * f2=initVar f4=initExpr f6=condExpr f8=stepVar f10=stepExpr f12=body
     */
    @Override
    public Object visit(ForStatement n, Object argu) {
        String initVar = n.f2.f0.tokenImage;
        CPValue initVal = evalExpression(n.f4);

        if (!isField(initVar))
            state.put(initVar, initVal);

        CPValue condVal = evalExpression(n.f6);
        if (condVal.isFalse())
            return null; // body never executes

        // State before the loop
        Map<String, CPValue> preLoop = new LinkedHashMap<>(state);

        // Iterate body + step until fixpoint
        boolean changed = true;
        while (changed) {
            Map<String, CPValue> before = new LinkedHashMap<>(state);

            n.f12.accept(this, argu); // body

            // step assignment
            String stepVar = n.f8.f0.tokenImage;
            CPValue stepVal = evalExpression(n.f10);
            if (!isField(stepVar))
                state.put(stepVar, stepVal);

            changed = !state.equals(before);
        }

        // Conservative join: loop may execute 0 times
        state = meetMaps(preLoop, state);
        return null;
    }

    @Override
    public Object visit(PrintStatement n, Object argu) {
        return null; // no state change
    }

    @Override
    public Object visit(Block n, Object argu) {
        runStatements(n.f1);
        return null;
    }

    // ---------------------------------------------------------------
    // Expression evaluator — returns CPValue, does NOT modify state
    // ---------------------------------------------------------------

    private CPValue evalExpression(Expression n) {
        Node choice = n.f0.choice;

        if (choice instanceof CompareExpression) {
            CompareExpression ce = (CompareExpression) choice;
            CPValue l = lookupVar(ce.f0.f0.tokenImage);
            CPValue r = lookupVar(ce.f2.f0.tokenImage);
            if (l.isInt() && r.isInt())
                return CPValue.ofBool(l.intValue() < r.intValue());
            return CPValue.NAC;

        } else if (choice instanceof PlusExpression) {
            PlusExpression pe = (PlusExpression) choice;
            CPValue l = lookupVar(pe.f0.f0.tokenImage);
            CPValue r = lookupVar(pe.f2.f0.tokenImage);
            if (l.isInt() && r.isInt())
                return CPValue.ofInt(l.intValue() + r.intValue());
            return CPValue.NAC;

        } else if (choice instanceof MinusExpression) {
            MinusExpression me = (MinusExpression) choice;
            CPValue l = lookupVar(me.f0.f0.tokenImage);
            CPValue r = lookupVar(me.f2.f0.tokenImage);
            if (l.isInt() && r.isInt())
                return CPValue.ofInt(l.intValue() - r.intValue());
            return CPValue.NAC;

        } else if (choice instanceof TimesExpression) {
            TimesExpression te = (TimesExpression) choice;
            CPValue l = lookupVar(te.f0.f0.tokenImage);
            CPValue r = lookupVar(te.f2.f0.tokenImage);
            if (l.isInt() && r.isInt())
                return CPValue.ofInt(l.intValue() * r.intValue());
            return CPValue.NAC;

        } else if (choice instanceof PrimaryExpression) {
            return evalPrimary((PrimaryExpression) choice);

        } else {
            // ArrayLookup, ArrayLength, MessageSend, FieldRead -> NAC
            return CPValue.NAC;
        }
    }

    private CPValue evalPrimary(PrimaryExpression n) {
        Node choice = n.f0.choice;
        if (choice instanceof IntegerLiteral)
            return CPValue.ofInt(Integer.parseInt(
                    ((IntegerLiteral) choice).f0.tokenImage));
        if (choice instanceof TrueLiteral)
            return CPValue.TRUE_VAL;
        if (choice instanceof FalseLiteral)
            return CPValue.FALSE_VAL;
        if (choice instanceof Identifier)
            return lookupVar(((Identifier) choice).f0.tokenImage);
        if (choice instanceof NotExpression) {
            CPValue v = lookupVar(((NotExpression) choice).f1.f0.tokenImage);
            if (v.isBool())
                return CPValue.ofBool(!v.boolValue());
            return CPValue.NAC;
        }
        // this, new Foo(), new int[n] -> NAC
        return CPValue.NAC;
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    /** Look up a variable: fields -> NAC, locals/params -> state map. */
    private CPValue lookupVar(String name) {
        if (isField(name))
            return CPValue.NAC;
        CPValue v = state.get(name);
        return v == null ? CPValue.UNDEF : v;
    }

    private boolean isField(String name) {
        String cur = currentClass;
        while (cur != null) {
            if (st.fieldsOf(cur).containsKey(name))
                return true;
            cur = ch.parentOf(cur);
        }
        return false;
    }

    /**
     * Component-wise meet of two state maps.
     * A variable absent from one map is treated as UNDEF in that map.
     */
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
}