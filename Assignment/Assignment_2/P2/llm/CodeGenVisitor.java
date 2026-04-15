import syntaxtree.*;
import visitor.*;
import java.util.Enumeration;

/**
 * Pass 2 — Code Generation Visitor.
 *
 * Translates a BuritoJava AST into TacoJava source code.
 *
 * Return type (R) = String
 * • For expression-visiting methods: returns a TacoJava expression string
 * (e.g., "a + b", "obj.method(x)", "true") or a simple identifier.
 * • For statement-visiting methods: returns null (side-effects into ctx).
 * • For type-visiting methods: returns the type string ("int", "boolean", …).
 *
 * Argument type (A) = CodeGenContext
 *
 * Key helper methods:
 * normalizeExprToIdent — ensures an Expression becomes an identifier (creates
 * temp if needed)
 * normalizePrimToIdent — same for PrimaryExpression
 * toIdent — wraps any expression string in a fresh temp
 * inferExprType — infers the TacoJava type of a BuritoJava expression node
 */
public class CodeGenVisitor extends GJDepthFirst<String, CodeGenContext> {

    // ================================================================
    // GOAL & CLASS STRUCTURE
    // ================================================================

    /**
     * Goal: MainClass() (TypeDeclaration())* EOF
     * f0=MainClass, f1=TypeDeclaration*, f2=EOF
     */
    @Override
    public String visit(Goal n, CodeGenContext ctx) {
        n.f0.accept(this, ctx);
        n.f1.accept(this, ctx);
        return null;
    }

    /**
     * BuritoJava MainClass:
     * "class" Identifier() "{" "public" "static" "void" "main"
     * "(" "String" "[" "]" Identifier() ")" "{" PrintStatement() "}" "}"
     * f1=className, f11=argsName, f14=PrintStatement
     *
     * TacoJava MainClass allows: VarDecl* Statement* (no explicit return).
     * We normalize the print expression to an identifier.
     */
    @Override
    public String visit(MainClass n, CodeGenContext ctx) {
        String className = n.f1.f0.tokenImage;
        String argsName = n.f11.f0.tokenImage;

        ctx.emitGlobal("class " + className + " {");
        ctx.emitGlobal("    public static void main(String[] " + argsName + ") {");

        ctx.enterMethodScope(className, "main");

        // Visit the single PrintStatement inside main
        n.f14.accept(this, ctx);

        String body = ctx.exitMethodScope();
        ctx.globalOutput.append(body);
        ctx.emitGlobal("    }");
        ctx.emitGlobal("}");
        ctx.emitGlobal("");
        return null;
    }

    /**
     * TypeDeclaration: ClassDeclaration | ClassExtendsDeclaration
     */
    @Override
    public String visit(TypeDeclaration n, CodeGenContext ctx) {
        n.f0.accept(this, ctx);
        return null;
    }

    /**
     * ClassDeclaration:
     * "class" Identifier() "{" (SimpleVarDeclaration)* (MethodDeclaration)* "}"
     * f1=className, f3=fields*, f4=methods*
     */
    @Override
    public String visit(ClassDeclaration n, CodeGenContext ctx) {
        String className = n.f1.f0.tokenImage;
        ctx.currentClass = className;
        ctx.emitGlobal("class " + className + " {");
        emitClassFields(n.f3, ctx); // SimpleVarDeclaration*
        n.f4.accept(this, ctx); // MethodDeclaration*
        ctx.emitGlobal("}");
        ctx.emitGlobal("");
        return null;
    }

    /**
     * ClassExtendsDeclaration:
     * "class" Identifier() "extends" Identifier() "{" (SimpleVarDeclaration)*
     * (MethodDeclaration)* "}"
     * f1=className, f3=superName, f5=fields*, f6=methods*
     */
    @Override
    public String visit(ClassExtendsDeclaration n, CodeGenContext ctx) {
        String className = n.f1.f0.tokenImage;
        String superName = n.f3.f0.tokenImage;
        ctx.currentClass = className;
        ctx.emitGlobal("class " + className + " extends " + superName + " {");
        emitClassFields(n.f5, ctx);
        n.f6.accept(this, ctx);
        ctx.emitGlobal("}");
        ctx.emitGlobal("");
        return null;
    }

    /** Emit each SimpleVarDeclaration as a TacoJava field. */
    private void emitClassFields(NodeListOptional fields, CodeGenContext ctx) {
        for (Enumeration<Node> e = fields.elements(); e.hasMoreElements();) {
            SimpleVarDeclaration svd = (SimpleVarDeclaration) e.nextElement();
            String type = typeToString(svd.f0);
            String name = svd.f1.f0.tokenImage;
            ctx.emitGlobal("    " + type + " " + name + ";");
        }
    }

    // ================================================================
    // METHOD DECLARATION
    // ================================================================

    /**
     * MethodDeclaration:
     * "public" Type() Identifier() "(" (FormalParameterList)? ")"
     * "{" (VarDeclaration)* (Statement)* "return" Expression() ";" "}"
     * f1=retType, f2=methName, f4=params?, f7=varDecls*, f8=stmts*, f10=returnExpr
     *
     * TacoJava MethodDeclaration: same structure but return must be Identifier.
     */
    @Override
    public String visit(MethodDeclaration n, CodeGenContext ctx) {
        String retType = typeToString(n.f1);
        String methName = n.f2.f0.tokenImage;
        String cls = ctx.currentClass;

        // Build parameter list string
        StringBuilder params = new StringBuilder();
        if (n.f4.present()) {
            params.append(formalParamListToString((FormalParameterList) n.f4.node));
        }

        // Emit method signature to global output
        ctx.emitGlobal("    public " + retType + " " + methName + "(" + params + ") {");

        // Enter method body scope
        ctx.enterMethodScope(cls, methName);

        // --- Process original var declarations ---
        // SimpleVarDeclaration -> emit as TacoJava VarDecl
        // FinalVarDeclaration -> emit decl + normalized assignment statement
        for (Enumeration<Node> e = n.f7.elements(); e.hasMoreElements();) {
            VarDeclaration vd = (VarDeclaration) e.nextElement();
            if (vd.f0.which == 0) {
                // SimpleVarDeclaration
                SimpleVarDeclaration svd = (SimpleVarDeclaration) vd.f0.choice;
                String type = typeToString(svd.f0);
                String vname = svd.f1.f0.tokenImage;
                ctx.emitVarDecl(type + " " + vname + ";");
            } else {
                // FinalVarDeclaration: "final" Type Identifier "=" Expression ";"
                FinalVarDeclaration fvd = (FinalVarDeclaration) vd.f0.choice;
                String type = typeToString(fvd.f1);
                String vname = fvd.f2.f0.tokenImage;
                ctx.emitVarDecl(type + " " + vname + ";");
                // Emit the initializer as an assignment statement
                String exprStr = visitExpression(fvd.f4, ctx);
                ctx.emitStmt(vname + " = " + exprStr + ";");
            }
        }

        // --- Process statements ---
        for (Enumeration<Node> e = n.f8.elements(); e.hasMoreElements();) {
            ((Statement) e.nextElement()).accept(this, ctx);
        }

        // --- Process return expression ---
        // TacoJava requires: return Identifier; — normalize if needed.
        String retExpr = visitExpression(n.f10, ctx);
        String retIdent = toIdent(retExpr, retType, ctx);
        ctx.emitStmt("return " + retIdent + ";");

        // Flush method body
        String body = ctx.exitMethodScope();
        ctx.globalOutput.append(body);
        ctx.emitGlobal("    }");
        ctx.emitGlobal("");
        return null;
    }

    /** Render a FormalParameterList as a comma-separated param string. */
    private String formalParamListToString(FormalParameterList fpl) {
        StringBuilder sb = new StringBuilder();
        // First param: f0=FormalParameter
        FormalParameter fp = fpl.f0;
        sb.append(typeToString(fp.f0)).append(" ").append(fp.f1.f0.tokenImage);
        // Rest: f1=NodeListOptional(FormalParameterRest*)
        for (Enumeration<Node> e = fpl.f1.elements(); e.hasMoreElements();) {
            FormalParameterRest fpr = (FormalParameterRest) e.nextElement();
            sb.append(", ")
                    .append(typeToString(fpr.f1.f0))
                    .append(" ")
                    .append(fpr.f1.f1.f0.tokenImage);
        }
        return sb.toString();
    }

    // ================================================================
    // STATEMENTS
    // ================================================================

    /**
     * Statement: NodeChoice with 8 alternatives (0..7)
     */
    @Override
    public String visit(Statement n, CodeGenContext ctx) {
        n.f0.accept(this, ctx);
        return null;
    }

    /**
     * Block: "{" (Statement)* "}"
     * f1=statements*
     */
    @Override
    public String visit(Block n, CodeGenContext ctx) {
        ctx.emitStmt("{");
        ctx.pushIndent();
        for (Enumeration<Node> e = n.f1.elements(); e.hasMoreElements();) {
            ((Statement) e.nextElement()).accept(this, ctx);
        }
        ctx.popIndent();
        ctx.emitStmt("}");
        return null;
    }

    /**
     * AssignmentStatement: Identifier() "=" Expression() ";"
     * f0=Identifier, f2=Expression
     * TacoJava: Identifier "=" Expression ";" — same structure, just normalize
     * expr.
     */
    @Override
    public String visit(AssignmentStatement n, CodeGenContext ctx) {
        String varName = n.f0.f0.tokenImage;
        String exprStr = visitExpression(n.f2, ctx);
        ctx.emitStmt(varName + " = " + exprStr + ";");
        return null;
    }

    /**
     * ArrayAssignmentStatement: Identifier() "[" Expression() "]" "=" Expression()
     * ";"
     * f0=array, f2=index expr, f5=value expr
     * TacoJava: Identifier "[" Identifier "]" "=" Identifier ";" — normalize both.
     */
    @Override
    public String visit(ArrayAssignmentStatement n, CodeGenContext ctx) {
        String arr = n.f0.f0.tokenImage;
        String idx = normalizeExprToIdent(n.f2, ctx);
        String val = normalizeExprToIdent(n.f5, ctx);
        ctx.emitStmt(arr + "[" + idx + "] = " + val + ";");
        return null;
    }

    /**
     * FieldAssignmentStatement: Expression() "." Identifier() "=" Expression() ";"
     * f0=obj expr, f2=field name, f4=value expr
     * TacoJava: Identifier "." Identifier "=" Identifier ";" — normalize all three.
     */
    @Override
    public String visit(FieldAssignmentStatement n, CodeGenContext ctx) {
        String obj = normalizeExprToIdent(n.f0, ctx);
        String field = n.f2.f0.tokenImage;
        String val = normalizeExprToIdent(n.f4, ctx);
        ctx.emitStmt(obj + "." + field + " = " + val + ";");
        return null;
    }

    /**
     * IfStatement: "if" "(" Expression() ")" Statement() "else" Statement()
     * f2=condition, f4=then-stmt, f6=else-stmt
     * TacoJava: condition must be Identifier.
     */
    @Override
    public String visit(IfStatement n, CodeGenContext ctx) {
        String cond = normalizeExprToIdent(n.f2, ctx);
        ctx.emitStmt("if (" + cond + ") {");
        ctx.pushIndent();
        n.f4.accept(this, ctx);
        ctx.popIndent();
        ctx.emitStmt("} else {");
        ctx.pushIndent();
        n.f6.accept(this, ctx);
        ctx.popIndent();
        ctx.emitStmt("}");
        return null;
    }

    /**
     * WhileStatement: "while" "(" Expression() ")" Statement()
     * f2=condition, f4=body
     * TacoJava: condition must be Identifier.
     */
    @Override
    public String visit(WhileStatement n, CodeGenContext ctx) {
        // 1. Evaluate condition before the loop
        String cond = normalizeExprToIdent(n.f2, ctx);

        ctx.emitStmt("while (" + cond + ") {");
        ctx.pushIndent();

        // 2. Execute the body of the loop
        n.f4.accept(this, ctx);

        // 3. Re-evaluate the expression at the end of the loop body
        // This emits any required setup temps into the loop body and
        // assigns the fresh evaluation back to our initial condition variable.
        String nextCond = visitExpression(n.f2, ctx);
        ctx.emitStmt(cond + " = " + nextCond + ";");

        ctx.popIndent();
        ctx.emitStmt("}");
        return null;
    }

    /**
     * ForStatement:
     * "for" "(" Identifier() "=" Expression() ";" Expression() ";"
     * Identifier() "=" Expression() ")" Statement()
     * f2=initVar, f4=initExpr, f6=condExpr, f8=updateVar, f10=updateExpr, f12=body
     *
     * TacoJava ForStatement has the same structure, so expressions can stay as
     * TacoJava Expressions (binary ops already use ident operands after
     * normalization).
     * Simple cases (identifier conditions) work perfectly; complex conditions whose
     * temps are set once may not re-evaluate — acceptable for typical for loops.
     */
    @Override
    public String visit(ForStatement n, CodeGenContext ctx) {
        String initVar = n.f2.f0.tokenImage;
        String initVal = visitExpression(n.f4, ctx);
        String cond = visitExpression(n.f6, ctx);
        String updateVar = n.f8.f0.tokenImage;
        String updateVal = visitExpression(n.f10, ctx);

        ctx.emitStmt("for (" + initVar + " = " + initVal + "; "
                + cond + "; "
                + updateVar + " = " + updateVal + ") {");
        ctx.pushIndent();
        n.f12.accept(this, ctx);
        ctx.popIndent();
        ctx.emitStmt("}");
        return null;
    }

    /**
     * PrintStatement: "System.out.println" "(" Expression() ")" ";"
     * f2=Expression
     * TacoJava: arg must be Identifier.
     */
    @Override
    public String visit(PrintStatement n, CodeGenContext ctx) {
        String arg = normalizeExprToIdent(n.f2, ctx);
        ctx.emitStmt("System.out.println(" + arg + ");");
        return null;
    }

    // ================================================================
    // EXPRESSIONS (return TacoJava expression string or identifier)
    // ================================================================

    /**
     * Expression: NodeChoice with 10 alternatives.
     * Delegates to the chosen sub-expression visitor.
     */
    @Override
    public String visit(Expression n, CodeGenContext ctx) {
        return (String) n.f0.accept(this, ctx);
    }

    // ---- Binary operators ----

    /**
     * ORExpression: PrimaryExpression() "|" PrimaryExpression()
     * No "|" in TacoJava — expand to if-else.
     * Returns identifier of a fresh boolean temp.
     */
    @Override
    public String visit(ORExpression n, CodeGenContext ctx) {
        String left = normalizePrimToIdent(n.f0, ctx);
        String right = normalizePrimToIdent(n.f2, ctx);
        String temp = ctx.newTemp("boolean");
        ctx.emitStmt("if (" + left + ") {");
        ctx.pushIndent();
        ctx.emitStmt(temp + " = true;");
        ctx.popIndent();
        ctx.emitStmt("} else {");
        ctx.pushIndent();
        ctx.emitStmt("if (" + right + ") {");
        ctx.pushIndent();
        ctx.emitStmt(temp + " = true;");
        ctx.popIndent();
        ctx.emitStmt("} else {");
        ctx.pushIndent();
        ctx.emitStmt(temp + " = false;");
        ctx.popIndent();
        ctx.emitStmt("}");
        ctx.popIndent();
        ctx.emitStmt("}");
        return temp;
    }

    /**
     * AndExpression: PrimaryExpression() "&" PrimaryExpression()
     * No "&" in TacoJava — expand to if-else.
     * Returns identifier of a fresh boolean temp.
     */
    @Override
    public String visit(AndExpression n, CodeGenContext ctx) {
        String left = normalizePrimToIdent(n.f0, ctx);
        String right = normalizePrimToIdent(n.f2, ctx);
        String temp = ctx.newTemp("boolean");
        ctx.emitStmt("if (" + left + ") {");
        ctx.pushIndent();
        ctx.emitStmt("if (" + right + ") {");
        ctx.pushIndent();
        ctx.emitStmt(temp + " = true;");
        ctx.popIndent();
        ctx.emitStmt("} else {");
        ctx.pushIndent();
        ctx.emitStmt(temp + " = false;");
        ctx.popIndent();
        ctx.emitStmt("}");
        ctx.popIndent();
        ctx.emitStmt("} else {");
        ctx.pushIndent();
        ctx.emitStmt(temp + " = false;");
        ctx.popIndent();
        ctx.emitStmt("}");
        return temp;
    }

    /**
     * CompareExpression: PrimaryExpression() "<" PrimaryExpression()
     * TacoJava: Identifier "<" Identifier — normalize operands.
     */
    @Override
    public String visit(CompareExpression n, CodeGenContext ctx) {
        String left = normalizePrimToIdent(n.f0, ctx);
        String right = normalizePrimToIdent(n.f2, ctx);
        return left + " < " + right;
    }

    /**
     * PlusExpression: PrimaryExpression() "+" PrimaryExpression()
     * TacoJava: Identifier "+" Identifier — normalize operands.
     */
    @Override
    public String visit(PlusExpression n, CodeGenContext ctx) {
        String left = normalizePrimToIdent(n.f0, ctx);
        String right = normalizePrimToIdent(n.f2, ctx);
        return left + " + " + right;
    }

    /**
     * MinusExpression: PrimaryExpression() "-" PrimaryExpression()
     */
    @Override
    public String visit(MinusExpression n, CodeGenContext ctx) {
        String left = normalizePrimToIdent(n.f0, ctx);
        String right = normalizePrimToIdent(n.f2, ctx);
        return left + " - " + right;
    }

    /**
     * TimesExpression: PrimaryExpression() "*" PrimaryExpression()
     */
    @Override
    public String visit(TimesExpression n, CodeGenContext ctx) {
        String left = normalizePrimToIdent(n.f0, ctx);
        String right = normalizePrimToIdent(n.f2, ctx);
        return left + " * " + right;
    }

    // ---- Array operations ----

    /**
     * ArrayLookup: PrimaryExpression() "[" PrimaryExpression() "]"
     * TacoJava: Identifier "[" Identifier "]" — normalize both.
     */
    @Override
    public String visit(ArrayLookup n, CodeGenContext ctx) {
        String arr = normalizePrimToIdent(n.f0, ctx);
        String idx = normalizePrimToIdent(n.f2, ctx);
        return arr + "[" + idx + "]";
    }

    /**
     * ArrayLength: PrimaryExpression() "." "length"
     * TacoJava: Identifier ".length" — normalize base.
     */
    @Override
    public String visit(ArrayLength n, CodeGenContext ctx) {
        String arr = normalizePrimToIdent(n.f0, ctx);
        return arr + ".length";
    }

    // ---- Method call ----

    /**
     * MessageSend: PrimaryExpression() "." Identifier() "(" (ExpressionList)? ")"
     * f0=receiver, f2=methodName, f4=args?
     *
     * TacoJava: PrimaryExpression "." Identifier "(" (ArgList)? ")"
     * where ArgList uses only Identifiers.
     * Receiver stays as PrimaryExpression (no normalization needed).
     * Arguments must be normalized to identifiers.
     */
    @Override
    public String visit(MessageSend n, CodeGenContext ctx) {
        String receiver = visitPrimary(n.f0, ctx); // TacoJava PrimaryExpression is fine
        String methName = n.f2.f0.tokenImage;

        StringBuilder args = new StringBuilder();
        if (n.f4.present()) {
            ExpressionList el = (ExpressionList) n.f4.node;
            // First arg
            args.append(normalizeExprToIdent(el.f0, ctx));
            // Remaining args
            for (Enumeration<Node> e = el.f1.elements(); e.hasMoreElements();) {
                ExpressionRest er = (ExpressionRest) e.nextElement();
                args.append(", ").append(normalizeExprToIdent(er.f1, ctx));
            }
        }

        return receiver + "." + methName + "(" + args + ")";
    }

    // ---- Primary expressions ----

    /**
     * PrimaryExpression: NodeChoice with 9 alternatives.
     *
     * Maps BuritoJava primaries to TacoJava equivalents:
     * IntegerLiteral, TrueLiteral, FalseLiteral → literal string
     * Identifier → identifier name
     * ThisExpression → "this"
     * ArrayAllocationExpression → "new int[ident]" (size normalized to ident)
     * AllocationExpression → "new Foo()"
     * NotExpression → "!ident" (operand normalized to ident)
     * BracketExpression → BuritoJava-only; inner expr normalized to ident
     */
    @Override
    public String visit(PrimaryExpression n, CodeGenContext ctx) {
        return visitPrimary(n, ctx);
    }

    /** Core helper used by MessageSend and normalizePrimToIdent. */
    private String visitPrimary(PrimaryExpression n, CodeGenContext ctx) {
        switch (n.f0.which) {

            case 0: // IntegerLiteral
                return ((IntegerLiteral) n.f0.choice).f0.tokenImage;

            case 1: // TrueLiteral
                return "true";

            case 2: // FalseLiteral
                return "false";

            case 3: // Identifier — already an identifier, return name directly
                return ((Identifier) n.f0.choice).f0.tokenImage;

            case 4: // ThisExpression
                return "this";

            case 5: { // ArrayAllocationExpression: "new" "int" "[" Expression "]"
                ArrayAllocationExpression aae = (ArrayAllocationExpression) n.f0.choice;
                // TacoJava requires Identifier inside brackets
                String size = normalizeExprToIdent(aae.f3, ctx);
                return "new int[" + size + "]";
            }

            case 6: { // AllocationExpression: "new" Identifier() "()"
                AllocationExpression ae = (AllocationExpression) n.f0.choice;
                return "new " + ae.f1.f0.tokenImage + "()";
            }

            case 7: { // NotExpression: "!" (MessageSend | PrimaryExpression)
                NotExpression ne = (NotExpression) n.f0.choice;
                String inner;
                if (ne.f1.which == 0) {
                    // MessageSend branch
                    inner = visit((MessageSend) ne.f1.choice, ctx);
                } else {
                    // PrimaryExpression branch
                    inner = visitPrimary((PrimaryExpression) ne.f1.choice, ctx);
                }
                // TacoJava NotExpression: "!" Identifier — normalize inner
                String identInner = toIdent(inner, "boolean", ctx);
                return "!" + identInner;
            }

            case 8: { // BracketExpression: "(" Expression ")"
                // BracketExpression does not exist in TacoJava.
                // Evaluate the inner expression and normalize to an identifier.
                BracketExpression be = (BracketExpression) n.f0.choice;
                return normalizeExprToIdent(be.f1, ctx);
            }

            default:
                return "null";
        }
    }

    // ================================================================
    // NORMALIZATION HELPERS
    // ================================================================

    /**
     * Visit an Expression node, returning a TacoJava expression string.
     * (This is a typed alias for n.accept(this, ctx) to avoid cast clutter.)
     */
    private String visitExpression(Expression n, CodeGenContext ctx) {
        return (String) n.accept(this, ctx);
    }

    /**
     * Ensure an Expression evaluates to a TacoJava identifier.
     * If the expression is already a simple identifier, return it directly.
     * Otherwise, assign the expression to a fresh temp and return the temp name.
     */
    private String normalizeExprToIdent(Expression n, CodeGenContext ctx) {
        String exprStr = visitExpression(n, ctx);
        if (isSimpleIdent(exprStr))
            return exprStr;
        // Need a temp
        String type = inferExprType(n, ctx);
        return toIdent(exprStr, type, ctx);
    }

    /**
     * Ensure a PrimaryExpression evaluates to a TacoJava identifier.
     * Identifiers and 'this' are returned as-is.
     * Literals and complex primaries are assigned to a fresh temp.
     */
    private String normalizePrimToIdent(PrimaryExpression n, CodeGenContext ctx) {
        String primStr = visitPrimary(n, ctx);
        if (isSimpleIdent(primStr))
            return primStr;
        // literals and complex expressions need a temp
        String type = inferPrimType(n, ctx);
        return toIdent(primStr, type, ctx);
    }

    /**
     * If exprStr is already a simple identifier, return it.
     * Otherwise emit " temp = exprStr; " and return temp name.
     */
    private String toIdent(String exprStr, String type, CodeGenContext ctx) {
        if (isSimpleIdent(exprStr))
            return exprStr;
        String temp = ctx.newTemp(type);
        ctx.emitStmt(temp + " = " + exprStr + ";");
        return temp;
    }

    // ================================================================
    // TYPE INFERENCE
    // ================================================================

    /** Infer the TacoJava type of a BuritoJava Expression node. */
    private String inferExprType(Expression n, CodeGenContext ctx) {
        Node choice = n.f0.choice;
        if (choice instanceof ORExpression)
            return "boolean";
        if (choice instanceof AndExpression)
            return "boolean";
        if (choice instanceof CompareExpression)
            return "boolean";
        if (choice instanceof PlusExpression)
            return "int";
        if (choice instanceof MinusExpression)
            return "int";
        if (choice instanceof TimesExpression)
            return "int";
        if (choice instanceof ArrayLookup)
            return "int";
        if (choice instanceof ArrayLength)
            return "int";
        if (choice instanceof ThisExpression) {
            // Because of the fix above, ctx.currentClass will now correctly be "Tree"
            return ctx.currentClass != null ? ctx.currentClass : "Object";
        }
        if (choice instanceof MessageSend) {
            return inferMessageSendType((MessageSend) choice, ctx);
        }
        if (choice instanceof PrimaryExpression) {
            return inferPrimType((PrimaryExpression) choice, ctx);
        }
        return "int"; // safe default
    }

    /** Infer the TacoJava type of a BuritoJava PrimaryExpression node. */
    private String inferPrimType(PrimaryExpression n, CodeGenContext ctx) {
        Node choice = n.f0.choice;
        if (choice instanceof IntegerLiteral)
            return "int";
        if (choice instanceof TrueLiteral)
            return "boolean";
        if (choice instanceof FalseLiteral)
            return "boolean";

        if (choice instanceof Identifier) {
            String name = ((Identifier) choice).f0.tokenImage;
            return ctx.symTable.lookupVarType(name, ctx.currentClass, ctx.currentMethod);
        }

        // Ensure 'this' correctly grabs the current class scope
        if (choice instanceof ThisExpression) {
            if (ctx.currentClass != null) {
                return ctx.currentClass;
            }
            return "Object";
        }

        if (choice instanceof ArrayAllocationExpression)
            return "int[]";
        if (choice instanceof AllocationExpression) {
            return ((AllocationExpression) choice).f1.f0.tokenImage;
        }
        if (choice instanceof NotExpression)
            return "boolean";
        if (choice instanceof BracketExpression) {
            return inferExprType(((BracketExpression) choice).f1, ctx);
        }
        return "int";
    }

    /**
     * Infer the return type of a MessageSend by looking up the method
     * in the symbol table. We first try the current class, then search
     * all classes (best-effort for untyped receivers).
     */
    private String inferMessageSendType(MessageSend ms, CodeGenContext ctx) {
        String methodName = ms.f2.f0.tokenImage;

        // 1. Infer the type of the receiver FIRST
        String receiverType = inferPrimType(ms.f0, ctx);

        // 2. Look up the method in that specific class
        if (receiverType != null && !"Object".equals(receiverType)) {
            String ret = ctx.symTable.getMethodReturnType(receiverType, methodName);
            if (!"Object".equals(ret)) {
                return ret;
            }
        }

        // 3. Fallback: Search all classes
        for (SymbolTable.ClassInfo ci : ctx.symTable.getAllClasses().values()) {
            SymbolTable.MethodInfo mi = ci.getMethod(methodName);
            if (mi != null)
                return mi.returnType;
        }
        return "Object";
    }

    // ================================================================
    // TYPE STRING CONVERSION
    // ================================================================

    /**
     * Convert a BuritoJava Type node to a TacoJava type string.
     * Type choices: 0=ArrayType, 1=BooleanType, 2=IntegerType, 3=Identifier
     */
    private String typeToString(Type t) {
        switch (t.f0.which) {
            case 0:
                return "int[]";
            case 1:
                return "boolean";
            case 2:
                return "int";
            case 3:
                return ((Identifier) t.f0.choice).f0.tokenImage;
            default:
                return "Object";
        }
    }

    // ================================================================
    // STRING UTILITIES
    // ================================================================

    /**
     * Returns true if s is a valid Java identifier (letter/$/_ followed by
     * letter/digit/$/_ only). Used to decide whether a temp is needed.
     */
    private boolean isSimpleIdent(String s) {
        if (s == null || s.isEmpty())
            return false;

        // Must reject these so they get extracted to temporary identifiers!
        if (s.equals("true") || s.equals("false") || s.equals("this") || s.equals("null")) {
            return false;
        }

        char first = s.charAt(0);
        if (!Character.isLetter(first) && first != '_' && first != '$')
            return false;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_' && c != '$')
                return false;
        }
        return true;
    }
}
