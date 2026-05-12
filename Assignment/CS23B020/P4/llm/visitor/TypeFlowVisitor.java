package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 2: Flow-sensitive, context-sensitive points-to analysis.
 *
 * Two maps:
 * stack : varName -> Set<concreteType> (locals, formals, "this")
 * heap : "ConcreteType.field" -> Set<concreteType> (field contents)
 *
 * Context-sensitivity: ANY monomorphic call (INLINE-annotated or not) causes
 * us to analyse the callee body with a fresh context. This is essential so
 * that INLINE sites nested inside non-INLINE-annotated callees are discovered.
 *
 * Recursion guard: inProgress prevents re-entering the same method.
 *
 * OUTPUT: inlineTargets maps INLINE-annotated, monomorphic
 * MessageSendStatement nodes -> concrete class to inline from.
 */
public class TypeFlowVisitor extends GJDepthFirst<String, Map<String, Set<String>>> {

    private final SymbolTableBuilder symtab;

    // Prevent infinite recursion: "ClassName.methodName"
    private final Set<String> inProgress = new HashSet<>();

    // OUTPUT
    public final Map<MessageSendStatement, String> inlineTargets = new IdentityHashMap<>();

    // Shared heap map across the whole analysis
    // Key: "ConcreteType.fieldName"
    private final Map<String, Set<String>> heap = new HashMap<>();

    public TypeFlowVisitor(SymbolTableBuilder symtab) {
        this.symtab = symtab;
    }

    // -------------------------------------------------------
    // Stack helpers
    // -------------------------------------------------------

    private Set<String> ptOf(String var, Map<String, Set<String>> stack) {
        return stack.getOrDefault(var, Collections.emptySet());
    }

    private void setPt(String var, Set<String> types, Map<String, Set<String>> stack) {
        stack.put(var, new HashSet<>(types));
    }

    private Map<String, Set<String>> copyStack(Map<String, Set<String>> stack) {
        Map<String, Set<String>> copy = new HashMap<>();
        for (Map.Entry<String, Set<String>> e : stack.entrySet())
            copy.put(e.getKey(), new HashSet<>(e.getValue()));
        return copy;
    }

    private void mergeStack(Map<String, Set<String>> dst, Map<String, Set<String>> src) {
        for (Map.Entry<String, Set<String>> e : src.entrySet())
            dst.computeIfAbsent(e.getKey(), k -> new HashSet<>()).addAll(e.getValue());
    }

    // -------------------------------------------------------
    // Heap helpers
    // -------------------------------------------------------

    private void heapWrite(String obj, String field,
            Set<String> valueTypes, Map<String, Set<String>> stack) {
        for (String cls : ptOf(obj, stack))
            heap.computeIfAbsent(cls + "." + field, k -> new HashSet<>())
                    .addAll(valueTypes);
    }

    private Set<String> heapRead(String obj, String field,
            Map<String, Set<String>> stack) {
        Set<String> result = new HashSet<>();
        for (String cls : ptOf(obj, stack)) {
            Set<String> stored = heap.get(cls + "." + field);
            if (stored != null) {
                result.addAll(stored);
            } else {
                // Fall back to declared field type (conservative)
                String declaredType = symtab.fieldType(cls, field);
                if (declaredType != null && isReferenceType(declaredType))
                    result.add(declaredType);
            }
        }
        return result;
    }

    // -------------------------------------------------------
    // Receiver resolution
    // -------------------------------------------------------

    private String resolveReceiver(MessageSend ms, Map<String, Set<String>> stack) {
        // ms.f0 is PrimaryExpression directly
        Node choice = ms.f0.f0.choice;
        String receiverName;
        if (choice instanceof Identifier)
            receiverName = ((Identifier) choice).f0.tokenImage;
        else if (choice instanceof ThisExpression)
            receiverName = "this";
        else
            return null;

        Set<String> types = ptOf(receiverName, stack);
        // System.err.println(receiverName + " : " + types);
        return (types.size() == 1) ? types.iterator().next() : null;
    }

    private boolean isReferenceType(String type) {
        return !type.equals("int") && !type.equals("boolean") && !type.equals("int[]");
    }

    private String returnType(String cls, String mname) {
        MethodInfo mi = symtab.lookupMethod(cls, mname);
        return (mi == null) ? null : mi.returnType;
    }

    // -------------------------------------------------------
    // Arg extraction
    // -------------------------------------------------------

    private List<String> extractArgs(MessageSend ms) {
        List<String> args = new ArrayList<>();
        if (!ms.f4.present())
            return args;
        ArgList al = (ArgList) ms.f4.node;
        args.add(constOrIdName(al.f0));
        if (al.f1.present())
            for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();)
                args.add(constOrIdName(((ArgRest) e.nextElement()).f1));
        return args;
    }

    private String constOrIdName(ConstOrId cid) {
        Node choice = cid.f0.choice;
        return (choice instanceof Identifier) ? ((Identifier) choice).f0.tokenImage : null;
    }

    // -------------------------------------------------------
    // Type / name helpers
    // -------------------------------------------------------

    @Override
    public String visit(Identifier n, Map<String, Set<String>> argu) {
        return n.f0.tokenImage;
    }

    @Override
    public String visit(Type n, Map<String, Set<String>> argu) {
        return n.f0.accept(this, argu);
    }

    @Override
    public String visit(ArrayType n, Map<String, Set<String>> argu) {
        return "int[]";
    }

    @Override
    public String visit(BooleanType n, Map<String, Set<String>> argu) {
        return "boolean";
    }

    @Override
    public String visit(IntegerType n, Map<String, Set<String>> argu) {
        return "int";
    }

    @Override
    public String visit(ConstOrId n, Map<String, Set<String>> argu) {
        Node c = n.f0.choice;
        return (c instanceof Identifier) ? ((Identifier) c).f0.tokenImage : null;
    }

    // -------------------------------------------------------
    // Top-level
    // -------------------------------------------------------

    @Override
    public String visit(Goal n, Map<String, Set<String>> argu) {
        Map<String, Set<String>> stack = new HashMap<>();
        n.f0.accept(this, stack); // MainClass
        // TypeDeclarations: no-op (methods analysed on demand)
        return null;
    }

    @Override
    public String visit(MainClass n, Map<String, Set<String>> argu) {
        Map<String, Set<String>> stack = new HashMap<>();
        n.f15.accept(this, stack);
        return null;
    }

    @Override
    public String visit(TypeDeclaration n, Map<String, Set<String>> argu) {
        return null; // analysed on demand from call sites
    }

    // -------------------------------------------------------
    // Statements
    // -------------------------------------------------------

    @Override
    public String visit(Statement n, Map<String, Set<String>> argu) {
        n.f0.accept(this, argu);
        return null;
    }

    @Override
    public String visit(Block n, Map<String, Set<String>> argu) {
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * AssignmentStatement: lhs = RhsExpression;
     * DotExpression (x.f) -> heap read
     * AllocationExpression -> {ClassName}
     * Identifier -> copy stack pt
     * ThisExpression -> copy "this" pt
     * arithmetic/literal -> {} (not a reference)
     */
    @Override
    public String visit(AssignmentStatement n, Map<String, Set<String>> argu) {
        String lhs = n.f0.f0.tokenImage;
        analyzeRhs(lhs, n.f2, argu);
        return null;
    }

    private void analyzeRhs(String lhs, RhsExpression rhs, Map<String, Set<String>> stack) {
        Node choice = rhs.f0.choice;
        if (choice instanceof DotExpression) {
            DotExpression de = (DotExpression) choice;
            setPt(lhs, heapRead(de.f0.f0.tokenImage, de.f2.f0.tokenImage, stack), stack);
        } else {
            analyzeExpression(lhs, (Expression) choice, stack);
        }
    }

    private void analyzeExpression(String lhs, Expression expr,
            Map<String, Set<String>> stack) {
        Node choice = expr.f0.choice;
        if (choice instanceof AllocationExpression) {
            setPt(lhs, Collections.singleton(((AllocationExpression) choice).f1.f0.tokenImage), stack);
        } else if (choice instanceof PrimaryExpression) {
            analyzePrimary(lhs, (PrimaryExpression) choice, stack);
        } else {
            setPt(lhs, Collections.emptySet(), stack);
        }
    }

    private void analyzePrimary(String lhs, PrimaryExpression pe,
            Map<String, Set<String>> stack) {
        Node choice = pe.f0.choice;
        if (choice instanceof Identifier)
            setPt(lhs, new HashSet<>(ptOf(((Identifier) choice).f0.tokenImage, stack)), stack);
        else if (choice instanceof ThisExpression)
            setPt(lhs, new HashSet<>(ptOf("this", stack)), stack);
        else if (choice instanceof AllocationExpression)
            setPt(lhs, Collections.singleton(((AllocationExpression) choice).f1.f0.tokenImage), stack);
        else
            setPt(lhs, Collections.emptySet(), stack);
    }

    @Override
    public String visit(ArrayAssignmentStatement n, Map<String, Set<String>> argu) {
        return null;
    }

    /**
     * FieldAssignmentStatement: obj.f = val;
     * Write pt(val) into heap for every concrete type of obj.
     */
    @Override
    public String visit(FieldAssignmentStatement n, Map<String, Set<String>> argu) {
        String obj = n.f0.f0.tokenImage;
        String field = n.f2.f0.tokenImage;
        String val = constOrIdName(n.f4);
        heapWrite(obj, field, val != null ? ptOf(val, argu) : Collections.emptySet(), argu);
        return null;
    }

    @Override
    public String visit(IfStatement n, Map<String, Set<String>> argu) {
        Map<String, Set<String>> ptThen = copyStack(argu);
        Map<String, Set<String>> ptElse = copyStack(argu);
        n.f4.accept(this, ptThen);
        n.f6.accept(this, ptElse);
        argu.clear();
        mergeStack(argu, ptThen);
        mergeStack(argu, ptElse);
        return null;
    }

    @Override
    public String visit(WhileStatement n, Map<String, Set<String>> argu) {
        Map<String, Set<String>> ptBody = copyStack(argu);
        n.f4.accept(this, ptBody);
        mergeStack(argu, ptBody);
        return null;
    }

    @Override
    public String visit(ForStatement n, Map<String, Set<String>> argu) {
        Map<String, Set<String>> ptBody = copyStack(argu);
        n.f12.accept(this, ptBody);
        mergeStack(argu, ptBody);
        return null;
    }

    @Override
    public String visit(PrintStatement n, Map<String, Set<String>> argu) {
        return null;
    }

    /**
     * MessageSendStatement:
     * f0 -> (InlineAnn)?
     * f1 -> NodeChoice(VoidMessageSendStmt | RetMessageSendStmt)
     *
     * 1. Resolve receiver concrete type.
     * 2. If INLINE + monomorphic -> record in inlineTargets.
     * 3. For ANY monomorphic call -> analyse callee body (context-sensitive)
     * so inner INLINE sites are discovered even when outer call is not INLINE.
     * 4. Update stack[lhs] with conservative return-type info.
     */
    @Override
    public String visit(MessageSendStatement n, Map<String, Set<String>> argu) {
        boolean hasInline = n.f0.present();

        MessageSend ms;
        String lhs = null;
        Node stmtChoice = n.f1.choice;
        if (stmtChoice instanceof RetMessageSendStmt) {
            RetMessageSendStmt rms = (RetMessageSendStmt) stmtChoice;
            lhs = rms.f0.f0.tokenImage;
            ms = rms.f2;
        } else {
            ms = ((VoidMessageSendStmt) stmtChoice).f0;
        }

        String methodName = ms.f2.f0.tokenImage;
        String concreteClass = resolveReceiver(ms, argu);

        if (concreteClass != null) {
            if (hasInline) {
                inlineTargets.put(n, concreteClass);
            }

            // Always recurse into callee (guarded against cycles)
            String callKey = concreteClass + "." + methodName;
            if (!inProgress.contains(callKey)) {
                MethodInfo mi = symtab.lookupMethod(concreteClass, methodName);
                if (mi != null) {
                    inProgress.add(callKey);
                    Map<String, Set<String>> calleePt = buildCalleeStack(ms, mi, concreteClass, argu);
                    mi.astNode.f8.accept(this, calleePt);
                    inProgress.remove(callKey);
                }
            }
        }

        // Update stack[lhs]
        if (lhs != null) {
            String retType = (concreteClass != null)
                    ? returnType(concreteClass, methodName)
                    : guessReturnType(ms, methodName, argu);
            if (retType != null && isReferenceType(retType))
                setPt(lhs, Collections.singleton(retType), argu);
            else
                setPt(lhs, Collections.emptySet(), argu);
        }

        return null;
    }

    private Map<String, Set<String>> buildCalleeStack(
            MessageSend ms, MethodInfo mi, String concreteClass,
            Map<String, Set<String>> callerStack) {

        Map<String, Set<String>> calleePt = new HashMap<>();
        calleePt.put("this", new HashSet<>(Collections.singleton(concreteClass)));
        List<String> args = extractArgs(ms);
        for (int i = 0; i < mi.paramNames.size() && i < args.size(); i++) {
            String arg = args.get(i);
            calleePt.put(mi.paramNames.get(i),
                    arg != null ? new HashSet<>(ptOf(arg, callerStack)) : new HashSet<>());
        }
        return calleePt;
    }

    private String guessReturnType(MessageSend ms, String methodName,
            Map<String, Set<String>> stack) {
        Node choice = ms.f0.f0.choice;
        String recv = null;
        if (choice instanceof Identifier)
            recv = ((Identifier) choice).f0.tokenImage;
        else if (choice instanceof ThisExpression)
            recv = "this";
        if (recv == null)
            return null;
        for (String cls : ptOf(recv, stack)) {
            MethodInfo mi = symtab.lookupMethod(cls, methodName);
            if (mi != null)
                return mi.returnType;
        }
        return null;
    }
}