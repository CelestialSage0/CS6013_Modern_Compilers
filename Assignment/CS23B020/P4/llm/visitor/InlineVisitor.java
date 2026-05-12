package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 3: Strictly pretty-prints and inlines based on Pass 2's context map.
 */
public class InlineVisitor extends GJDepthFirst<String, InlineContext> {

    @Override
    public String visit(Goal n, InlineContext ctx) {
        n.f0.accept(this, ctx);
        n.f1.accept(this, ctx);
        return null;
    }

    @Override
    public String visit(MainClass n, InlineContext ctx) {
        ctx.currentClass = n.f1.accept(this, ctx);
        ctx.callStack.clear();

        ctx.currentMethod = ctx.classTable.get(ctx.currentClass).lookupMethod("main", ctx.classTable);

        ctx.emitLine("class " + ctx.currentClass + " {");
        ctx.indent();
        ctx.emitLine("public static void main (String[] args) {");
        ctx.indent();

        StringBuilder saved = ctx.sb;
        ctx.sb = new StringBuilder();
        n.f15.accept(this, ctx);
        String stmts = ctx.sb.toString();
        ctx.sb = saved;

        n.f14.accept(this, ctx);

        for (String d : ctx.pendingVarDecls) {
            ctx.emitLine(d);
        }
        ctx.pendingVarDecls.clear();

        ctx.emit(stmts);

        ctx.dedent();
        ctx.emitLine("}");
        ctx.dedent();
        ctx.emitLine("}");
        return null;
    }

    @Override
    public String visit(TypeDeclaration n, InlineContext ctx) {
        n.f0.accept(this, ctx);
        return null;
    }

    @Override
    public String visit(ClassDeclaration n, InlineContext ctx) {
        ctx.currentClass = n.f1.accept(this, ctx);
        ctx.emitLine("class " + ctx.currentClass + " {");
        ctx.indent();
        n.f3.accept(this, ctx);
        n.f4.accept(this, ctx);
        ctx.dedent();
        ctx.emitLine("}");
        return null;
    }

    @Override
    public String visit(ClassExtendsDeclaration n, InlineContext ctx) {
        ctx.currentClass = n.f1.accept(this, ctx);
        ctx.emitLine("class " + ctx.currentClass + " extends " + n.f3.accept(this, ctx) + " {");
        ctx.indent();
        n.f5.accept(this, ctx);
        n.f6.accept(this, ctx);
        ctx.dedent();
        ctx.emitLine("}");
        return null;
    }

    @Override
    public String visit(VarDeclaration n, InlineContext ctx) {
        ctx.emitLine(n.f0.accept(this, ctx) + " " + n.f1.accept(this, ctx) + ";");
        return null;
    }

    @Override
    public String visit(MethodDeclaration n, InlineContext ctx) {
        String retType = n.f1.accept(this, ctx);
        String methName = n.f2.accept(this, ctx);
        String params = n.f4.present() ? n.f4.node.accept(this, ctx) : "";

        ctx.currentMethod = ctx.classTable.get(ctx.currentClass).lookupMethod(methName, ctx.classTable);

        ctx.emitLine("public " + retType + " " + methName + " (" + params + ") {");
        ctx.indent();

        StringBuilder saved = ctx.sb;
        ctx.sb = new StringBuilder();
        n.f8.accept(this, ctx);
        String stmts = ctx.sb.toString();
        ctx.sb = saved;

        n.f7.accept(this, ctx);
        for (String d : ctx.pendingVarDecls) {
            ctx.emitLine(d);
        }
        ctx.pendingVarDecls.clear();
        ctx.emit(stmts);

        ctx.emitLine("return " + n.f10.accept(this, ctx) + ";");
        ctx.dedent();
        ctx.emitLine("}");
        return null;
    }

    @Override
    public String visit(FormalParameterList n, InlineContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.f0.accept(this, ctx));
        for (Enumeration<Node> e = n.f1.elements(); e.hasMoreElements();)
            sb.append(e.nextElement().accept(this, ctx));
        return sb.toString();
    }

    @Override
    public String visit(FormalParameter n, InlineContext ctx) {
        return n.f0.accept(this, ctx) + " " + n.f1.accept(this, ctx);
    }

    @Override
    public String visit(FormalParameterRest n, InlineContext ctx) {
        return ", " + n.f1.accept(this, ctx);
    }

    @Override
    public String visit(Type n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(ArrayType n, InlineContext ctx) {
        return "int[]";
    }

    @Override
    public String visit(BooleanType n, InlineContext ctx) {
        return "boolean";
    }

    @Override
    public String visit(IntegerType n, InlineContext ctx) {
        return "int";
    }

    @Override
    public String visit(Statement n, InlineContext ctx) {
        n.f0.accept(this, ctx);
        return null;
    }

    @Override
    public String visit(Block n, InlineContext ctx) {
        ctx.emitLine("{");
        ctx.indent();
        n.f1.accept(this, ctx);
        ctx.dedent();
        ctx.emitLine("}");
        return null;
    }

    @Override
    public String visit(AssignmentStatement n, InlineContext ctx) {
        ctx.emitLine(ctx.subst(n.f0.f0.tokenImage) + " = " + n.f2.accept(this, ctx) + ";");
        return null;
    }

    @Override
    public String visit(ArrayAssignmentStatement n, InlineContext ctx) {
        ctx.emitLine(
                ctx.subst(n.f0.f0.tokenImage) + " [" + n.f2.accept(this, ctx) + "] = " + n.f5.accept(this, ctx) + ";");
        return null;
    }

    @Override
    public String visit(FieldAssignmentStatement n, InlineContext ctx) {
        ctx.emitLine(ctx.subst(n.f0.f0.tokenImage) + "." + n.f2.f0.tokenImage + " = " + n.f4.accept(this, ctx) + ";");
        return null;
    }

    @Override
    public String visit(IfStatement n, InlineContext ctx) {
        ctx.emitLine("if (" + ctx.subst(n.f2.f0.tokenImage) + ")");
        ctx.indent();
        n.f4.accept(this, ctx);
        ctx.dedent();
        ctx.emitLine("else");
        ctx.indent();
        n.f6.accept(this, ctx);
        ctx.dedent();
        return null;
    }

    @Override
    public String visit(WhileStatement n, InlineContext ctx) {
        ctx.emitLine("while (" + ctx.subst(n.f2.f0.tokenImage) + ")");
        ctx.indent();
        n.f4.accept(this, ctx);
        ctx.dedent();
        return null;
    }

    @Override
    public String visit(ForStatement n, InlineContext ctx) {
        String initId = ctx.subst(n.f2.f0.tokenImage);
        String initE = n.f4.accept(this, ctx);
        String cond = n.f6.accept(this, ctx);
        String incId = ctx.subst(n.f8.f0.tokenImage);
        String incE = n.f10.accept(this, ctx);
        ctx.emitLine("for (" + initId + " = " + initE + " ; " + cond + " ; " + incId + " = " + incE + ")");
        ctx.indent();
        n.f12.accept(this, ctx);
        ctx.dedent();
        return null;
    }

    @Override
    public String visit(PrintStatement n, InlineContext ctx) {
        ctx.emitLine("System.out.println (" + n.f2.accept(this, ctx) + ");");
        return null;
    }

    @Override
    public String visit(MessageSendStatement n, InlineContext ctx) {
        boolean doInline = n.f0.present();
        Node sendNode = n.f1.choice;
        MessageSend ms;
        String lhsVar = null;

        if (sendNode instanceof RetMessageSendStmt) {
            RetMessageSendStmt r = (RetMessageSendStmt) sendNode;
            ms = r.f2;
            lhsVar = r.f0.f0.tokenImage;
        } else {
            ms = ((VoidMessageSendStmt) sendNode).f0;
        }

        String receiver = ms.f0.accept(this, ctx);
        String methodName = ms.f2.f0.tokenImage;
        List<String> args = collectArgs(ms.f4, ctx);

        boolean tryInline = doInline || !ctx.callStack.isEmpty();
        InlineContext.CallPath path = new InlineContext.CallPath(ctx.callStack, ms);
        String concreteClass = ctx.callSiteResolutions.get(path);

        if (tryInline && concreteClass != null && !concreteClass.equals("POLYMORPHIC")) {
            ClassInfo ci = ctx.classTable.get(concreteClass);
            MethodInfo mi = ci.lookupMethod(methodName, ctx.classTable);

            String declaredType = ctx.getDeclaredType(receiver);
            boolean isTypeSafe = ctx.isSubtype(declaredType, mi.ownerClass);

            // ==============================================================================
            // CRITICAL FIX: Pure methods (no 'this' usage) are safe to inline anywhere!
            // ==============================================================================
            boolean canInline = isTypeSafe;
            if (!canInline) {
                HasThisVisitor htv = new HasThisVisitor();
                mi.astNode.accept(htv, ctx);
                if (!htv.hasThis) {
                    canInline = true;
                }
            }

            if (ctx.recursiveMethods.contains(mi) || !canInline) {
                emitCall(ctx, lhsVar, receiver, methodName, args); // Fallback to call
            } else {
                inlineMethod(ctx, mi, receiver, args, lhsVar, ms); // Safe to inline
            }
        } else {
            emitCall(ctx, lhsVar, receiver, methodName, args);
        }
        return null;
    }

    private void inlineMethod(InlineContext ctx, MethodInfo mi, String receiver, List<String> callArgs,
            String rawLhsVar, MessageSend msNode) {

        String resolvedLhs = (rawLhsVar != null) ? ctx.subst(rawLhsVar) : null;
        Map<String, String> savedSubst = new LinkedHashMap<>(ctx.varSubst);
        Map<String, String> newSubst = new LinkedHashMap<>(ctx.varSubst);

        for (int i = 0; i < mi.params.size(); i++) {
            String pname = mi.params.get(i)[1];
            String ptype = mi.params.get(i)[0];
            String arg = (i < callArgs.size()) ? callArgs.get(i) : "0";
            String tmp = ctx.freshTemp(pname, ptype);
            ctx.pendingVarDecls.add(ptype + " " + tmp + ";");
            ctx.emitLine(tmp + " = " + arg + ";");
            newSubst.put(pname, tmp);
        }

        for (String[] loc : mi.locals) {
            String lname = loc[1];
            String ltype = loc[0];
            String tmp = ctx.freshTemp(lname, ltype);
            ctx.pendingVarDecls.add(ltype + " " + tmp + ";");
            newSubst.put(lname, tmp);
        }

        newSubst.put("this", receiver);

        // ==============================================================================
        // CRITICAL FIX: Map bare field accesses directly to the receiver object!
        // This ensures heap reads are dynamic and never cached as stale temps.
        // ==============================================================================
        ClassInfo ci = ctx.classTable.get(mi.ownerClass);
        while (ci != null) {
            for (String fieldName : ci.fields.keySet()) {
                // Map the field only if it's not shadowed by a method parameter or local
                // variable
                if (!newSubst.containsKey(fieldName)) {
                    newSubst.put(fieldName, receiver + "." + fieldName);
                }
            }
            if (ci.parent == null)
                break;
            ci = ctx.classTable.get(ci.parent);
        }

        ctx.varSubst = newSubst;

        ctx.activeMethods.add(mi);
        ctx.emitLine("{");
        ctx.indent();
        ctx.callStack.push(msNode);

        for (Enumeration<Node> e = mi.astNode.f8.elements(); e.hasMoreElements();) {
            e.nextElement().accept(this, ctx);
        }

        ctx.callStack.pop();
        ctx.dedent();
        ctx.emitLine("}");
        ctx.activeMethods.remove(mi);

        if (resolvedLhs != null) {
            String returnValue = mi.astNode.f10.accept(this, ctx);
            ctx.emitLine(resolvedLhs + " = " + returnValue + ";");
        }

        ctx.varSubst = savedSubst;
    }

    private void emitCall(InlineContext ctx, String lhsVar, String receiver, String methodName, List<String> args) {
        String call = receiver + "." + methodName + " (" + String.join(", ", args) + ")";
        if (lhsVar != null)
            ctx.emitLine(ctx.subst(lhsVar) + " = " + call + ";");
        else
            ctx.emitLine(call + ";");
    }

    @Override
    public String visit(Expression n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(AndExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx) + " & " + n.f2.accept(this, ctx);
    }

    @Override
    public String visit(CompareExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx) + " < " + n.f2.accept(this, ctx);
    }

    @Override
    public String visit(PlusExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx) + " + " + n.f2.accept(this, ctx);
    }

    @Override
    public String visit(MinusExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx) + " - " + n.f2.accept(this, ctx);
    }

    @Override
    public String visit(TimesExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx) + " * " + n.f2.accept(this, ctx);
    }

    @Override
    public String visit(ArrayLookup n, InlineContext ctx) {
        return ctx.subst(n.f0.f0.tokenImage) + " [" + n.f2.accept(this, ctx) + "]";
    }

    @Override
    public String visit(ArrayLength n, InlineContext ctx) {
        return ctx.subst(n.f0.f0.tokenImage) + ".length";
    }

    @Override
    public String visit(PrimaryExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(IntegerLiteral n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(PlainIntegerLiteral n, InlineContext ctx) {
        return n.f0.tokenImage;
    }

    @Override
    public String visit(IntegerLiteralWithPosSign n, InlineContext ctx) {
        return "+" + n.f1.tokenImage;
    }

    @Override
    public String visit(IntegerLiteralWithNegSign n, InlineContext ctx) {
        return "-" + n.f1.tokenImage;
    }

    @Override
    public String visit(TrueLiteral n, InlineContext ctx) {
        return "true";
    }

    @Override
    public String visit(FalseLiteral n, InlineContext ctx) {
        return "false";
    }

    @Override
    public String visit(ThisExpression n, InlineContext ctx) {
        return ctx.subst("this");
    }

    @Override
    public String visit(AllocationExpression n, InlineContext ctx) {
        return "new " + n.f1.accept(this, ctx) + " ()";
    }

    @Override
    public String visit(ArrayAllocationExpression n, InlineContext ctx) {
        return "new int [" + n.f3.accept(this, ctx) + "]";
    }

    @Override
    public String visit(NotExpression n, InlineContext ctx) {
        return "!" + ctx.subst(n.f1.accept(this, ctx));
    }

    @Override
    public String visit(DotExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx) + "." + n.f2.f0.tokenImage;
    }

    @Override
    public String visit(RhsExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(ConstOrId n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(Identifier n, InlineContext ctx) {
        return ctx.subst(n.f0.tokenImage);
    }

    @Override
    public String visit(MessageSend n, InlineContext ctx) {
        List<String> args = collectArgs(n.f4, ctx);
        return n.f0.accept(this, ctx) + "." + n.f2.f0.tokenImage + " (" + String.join(", ", args) + ")";
    }

    private List<String> collectArgs(NodeOptional argListOpt, InlineContext ctx) {
        List<String> result = new ArrayList<>();
        if (!argListOpt.present())
            return result;
        ArgList al = (ArgList) argListOpt.node;
        result.add(al.f0.accept(this, ctx));
        for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();)
            result.add(((ArgRest) e.nextElement()).f1.accept(this, ctx));
        return result;
    }
}

// ==============================================================================
// Utility Visitor to scan for 'this' usage in a MethodDeclaration
// ==============================================================================
class HasThisVisitor extends GJDepthFirst<String, InlineContext> {
    public boolean hasThis = false;

    @Override
    public String visit(ThisExpression n, InlineContext ctx) {
        hasThis = true;
        return null;
    }
}