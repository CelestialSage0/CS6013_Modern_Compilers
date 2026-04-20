package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 6: Code Generation — pretty-prints the optimized AST as TACoJava2.
 *
 * Grammar field layout (from tacojava.jj / GJDepthFirst):
 * MainClass:
 * f0="class" f1=name(Id) f2="{" f3="public" f4="static" f5="void"
 * f6="main" f7="(" f8="String" f9="[" f10="]" f11=argsId(Id) f12=")"
 * f13="{" f14=VarDecls(NLO) f15=Stmts(NLO) f16="}" f17="}"
 * ArrayAssignmentStatement:
 * f0=arr(Id) f1="[" f2=index(Id) f3="]" f4="=" f5=val(Id) f6=";"
 * FieldAssignmentStatement:
 * f0=obj(Id) f1="." f2=field(Id) f3="=" f4=val(Id) f5=";"
 * IfStatement:
 * f0="if" f1="(" f2=cond(Id) f3=")" f4=thenStmt f5="else" f6=elseStmt
 * WhileStatement:
 * f0="while" f1="(" f2=cond(Id) f3=")" f4=body
 * ForStatement:
 * f0="for" f1="(" f2=initVar(Id) f3="=" f4=initExpr f5=";"
 * f6=condExpr f7=";" f8=stepVar(Id) f9="=" f10=stepExpr f11=")" f12=body
 * MessageSend:
 * f0=receiver(PrimaryExpr) f1="." f2=method(Id) f3="(" f4=ArgList? f5=")"
 * ArgList: f0=firstArg(Id) f1=( ArgRest() )*
 * ArgRest: f0="," f1=arg(Id)
 *
 * Dead-branch rule (from DeadCodeVisitor):
 * getDeadBranch(if) == "else" -> condition was true -> emit then-branch only
 * getDeadBranch(if) == "then" -> condition was false -> emit else-branch only
 *
 * When the surviving branch of a dead-if is a Block, we UNWRAP the block
 * and emit its statements directly (no extra braces).
 *
 * All dead-node queries use the INNER concrete node (unwrapped from Statement).
 */
public class CodeGen extends GJDepthFirst {

    private final ClassHierarchy ch;
    private final SymbolTable st;
    private final ConstPropVisitor cp;
    private final DeadCodeVisitor dc;

    private final StringBuilder out = new StringBuilder();
    private int indent = 0;
    private static final String INDENT = "   "; // 3 spaces

    private String currentClass;
    private String currentMethod;

    public CodeGen(ClassHierarchy ch,
            SymbolTable st,
            ConstPropVisitor cp,
            DeadCodeVisitor dc) {
        this.ch = ch;
        this.st = st;
        this.cp = cp;
        this.dc = dc;
    }

    public void flush() {
        System.out.print(out);
    }

    // ---------------------------------------------------------------
    // Output helpers
    // ---------------------------------------------------------------
    private void emit(String s) {
        out.append(s);
    }

    private void emitLn(String s) {
        out.append(s).append("\n");
    }

    private void nl() {
        out.append("\n");
    }

    private String ind() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++)
            sb.append(INDENT);
        return sb.toString();
    }

    // ---------------------------------------------------------------
    // Goal
    // ---------------------------------------------------------------
    @Override
    public Object visit(Goal n, Object argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return null;
    }

    // ---------------------------------------------------------------
    // MainClass
    // f1=className f11=argsId f14=VarDecls f15=Stmts
    // ---------------------------------------------------------------
    @Override
    public Object visit(MainClass n, Object argu) {
        currentClass = n.f1.f0.tokenImage;
        currentMethod = "main";

        emitLn("class " + currentClass + " {");
        indent++;
        emitLn(ind() + "public static void main(String [] "
                + n.f11.f0.tokenImage + ") {");
        indent++;
        emitVarDeclList(n.f14);
        emitStatementList(n.f15);
        indent--;
        emitLn(ind() + "}");
        indent--;
        emitLn("}");
        nl();

        currentClass = null;
        currentMethod = null;
        return null;
    }

    // ---------------------------------------------------------------
    // TypeDeclaration
    // ---------------------------------------------------------------
    @Override
    public Object visit(TypeDeclaration n, Object argu) {
        n.f0.accept(this, argu);
        return null;
    }

    // ---------------------------------------------------------------
    // ClassDeclaration
    // f1=name f3=VarDecls f4=MethodDecls
    // ---------------------------------------------------------------
    @Override
    public Object visit(ClassDeclaration n, Object argu) {
        currentClass = n.f1.f0.tokenImage;
        emitLn("class " + currentClass + " {");
        indent++;
        emitVarDeclList(n.f3);
        n.f4.accept(this, argu);
        indent--;
        emitLn("}");
        nl();
        currentClass = null;
        return null;
    }

    // ---------------------------------------------------------------
    // ClassExtendsDeclaration
    // f1=name f3=parent f5=VarDecls f6=MethodDecls
    // ---------------------------------------------------------------
    @Override
    public Object visit(ClassExtendsDeclaration n, Object argu) {
        currentClass = n.f1.f0.tokenImage;
        emitLn("class " + currentClass + " extends " + n.f3.f0.tokenImage + " {");
        indent++;
        emitVarDeclList(n.f5);
        n.f6.accept(this, argu);
        indent--;
        emitLn("}");
        nl();
        currentClass = null;
        return null;
    }

    // ---------------------------------------------------------------
    // VarDeclaration
    // f0=Type f1=name(Id) f2=";"
    // Dropped when inside a method and the variable is never live at
    // any program point (always substituted by CP or genuinely unused).
    // Field declarations (currentMethod == null) are always kept.
    // ---------------------------------------------------------------
    @Override
    public Object visit(VarDeclaration n, Object argu) {
        String varName = n.f1.f0.tokenImage;
        if (currentMethod != null
                && dc.isDeadVarDecl(currentClass, currentMethod, varName))
            return null;
        emit(ind());
        emitType(n.f0);
        emitLn(" " + varName + ";");
        return null;
    }

    // ---------------------------------------------------------------
    // MethodDeclaration
    // f1=Type f2=name f4=FormalParams? f7=VarDecls f8=Stmts f10=retVar
    // ---------------------------------------------------------------
    @Override
    public Object visit(MethodDeclaration n, Object argu) {
        String methodName = n.f2.f0.tokenImage;
        if (dc.isDeadMethod(currentClass, methodName))
            return null;

        currentMethod = methodName;
        emit(ind() + "public ");
        emitType(n.f1);
        emit(" " + methodName + "(");
        n.f4.accept(this, argu); // FormalParameterList (NodeOptional)
        emitLn(") {");
        indent++;
        emitVarDeclList(n.f7);
        emitStatementList(n.f8);
        emitLn(ind() + "return " + constOrId(n.f10.f0.tokenImage) + ";");
        indent--;
        emitLn(ind() + "}");
        nl();
        currentMethod = null;
        return null;
    }

    // ---------------------------------------------------------------
    // Formal parameters
    // ---------------------------------------------------------------
    @Override
    public Object visit(FormalParameterList n, Object argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return null;
    }

    @Override
    public Object visit(FormalParameter n, Object argu) {
        emitType(n.f0);
        emit(" " + n.f1.f0.tokenImage);
        return null;
    }

    @Override
    public Object visit(FormalParameterRest n, Object argu) {
        emit(", ");
        n.f1.accept(this, argu);
        return null;
    }

    // ---------------------------------------------------------------
    // Statement emission
    // All statement lists go through emitStatementList -> emitStatement.
    // We never override visit(Statement) or visit(NodeListOptional)
    // for statement routing — those are handled by explicit calls below.
    // ---------------------------------------------------------------

    /**
     * Emit all statements in a NodeListOptional.
     * Each node is a Statement wrapper; route through emitStatement.
     */
    private void emitStatementList(NodeListOptional nlo) {
        if (!nlo.present())
            return;
        for (Enumeration<Node> e = nlo.elements(); e.hasMoreElements();)
            emitStatement(e.nextElement());
    }

    /**
     * Emit a single statement.
     * Accepts Statement wrapper or already-unwrapped inner node.
     * Handles dead-code elimination before dispatching.
     */
    private void emitStatement(Node node) {
        Node inner = unwrap(node);

        if (inner instanceof AssignmentStatement) {
            emitAssignment((AssignmentStatement) inner);

        } else if (inner instanceof ArrayAssignmentStatement) {
            emitArrayAssign((ArrayAssignmentStatement) inner);

        } else if (inner instanceof FieldAssignmentStatement) {
            emitFieldAssign((FieldAssignmentStatement) inner);

        } else if (inner instanceof IfStatement) {
            emitIf((IfStatement) inner);

        } else if (inner instanceof WhileStatement) {
            emitWhile((WhileStatement) inner);

        } else if (inner instanceof ForStatement) {
            emitFor((ForStatement) inner);

        } else if (inner instanceof PrintStatement) {
            emitPrint((PrintStatement) inner);

        } else if (inner instanceof Block) {
            emitBlock((Block) inner);
        }
    }

    // ---------------------------------------------------------------
    // Concrete statement emitters
    // ---------------------------------------------------------------

    private void emitAssignment(AssignmentStatement as) {
        if (dc.isDeadAssignment(as)) {
            // If RHS is a MessageSend, promote to void call (has side effects)
            if (as.f2.f0.choice instanceof MessageSend) {
                emit(ind());
                emitMessageSend((MessageSend) as.f2.f0.choice);
                emitLn(";");
            }
            // Pure RHS -> drop entirely
            return;
        }
        emit(ind() + as.f0.f0.tokenImage + " = ");
        emitExpression(as.f2);
        emitLn(";");
    }

    private void emitArrayAssign(ArrayAssignmentStatement aas) {
        // f0=arr f2=index(Id) f5=val(Id)
        emitLn(ind() + aas.f0.f0.tokenImage
                + "[" + constOrId(aas.f2.f0.tokenImage) + "] = "
                + constOrId(aas.f5.f0.tokenImage) + ";");
    }

    private void emitFieldAssign(FieldAssignmentStatement fas) {
        // f0=obj f2=field(Id) f4=val(Id)
        emitLn(ind() + fas.f0.f0.tokenImage + "." + fas.f2.f0.tokenImage
                + " = " + constOrId(fas.f4.f0.tokenImage) + ";");
    }

    private void emitIf(IfStatement is) {
        String dead = dc.getDeadBranch(is);

        if ("else".equals(dead)) {
            // Condition was true -> emit then-branch, unwrapping any Block
            emitBranchBody(is.f4);
        } else if ("then".equals(dead)) {
            // Condition was false -> emit else-branch, unwrapping any Block
            emitBranchBody(is.f6);
        } else {
            // Both branches live -> full if/else
            emitLn(ind() + "if (" + is.f2.f0.tokenImage + ")");
            indent++;
            emitStatement(is.f4);
            indent--;
            emitLn(ind() + "else");
            indent++;
            emitStatement(is.f6);
            indent--;
        }
    }

    /**
     * Emit the body of a surviving dead-if branch.
     * If the branch IS a Block, emit its statements directly (no extra braces).
     * This avoids the spurious "{ x = 5; }" wrapper in the output.
     */
    private void emitBranchBody(Node branchNode) {
        Node inner = unwrap(branchNode);
        if (inner instanceof Block) {
            // Unwrap: emit contents directly at current indent
            emitStatementList(((Block) inner).f1);
        } else {
            emitStatement(branchNode);
        }
    }

    private void emitWhile(WhileStatement ws) {
        if (dc.isDeadWhile(ws))
            return; // condition always false -> drop
        emitLn(ind() + "while (" + ws.f2.f0.tokenImage + ")");
        indent++;
        emitStatement(ws.f4);
        indent--;
    }

    private void emitFor(ForStatement fs) {
        // f2=initVar f4=initExpr f6=condExpr f8=stepVar f10=stepExpr f12=body
        emit(ind() + "for (" + fs.f2.f0.tokenImage + " = ");
        emitExpression(fs.f4);
        emit("; ");
        emitExpression(fs.f6);
        emit("; " + fs.f8.f0.tokenImage + " = ");
        emitExpression(fs.f10);
        emitLn(")");
        indent++;
        emitStatement(fs.f12);
        indent--;
    }

    private void emitPrint(PrintStatement ps) {
        emitLn(ind() + "System.out.println(" + constOrId(ps.f2.f0.tokenImage) + ");");
    }

    private void emitBlock(Block b) {
        emitLn(ind() + "{");
        indent++;
        emitStatementList(b.f1);
        indent--;
        emitLn(ind() + "}");
    }

    // ---------------------------------------------------------------
    // Expression emitters
    // ---------------------------------------------------------------

    private void emitExpression(Expression n) {
        Node choice = n.f0.choice;

        if (choice instanceof CompareExpression) {
            CompareExpression ce = (CompareExpression) choice;
            emit(constOrId(ce.f0.f0.tokenImage) + " < "
                    + constOrId(ce.f2.f0.tokenImage));

        } else if (choice instanceof PlusExpression) {
            PlusExpression pe = (PlusExpression) choice;
            emit(constOrId(pe.f0.f0.tokenImage) + " + "
                    + constOrId(pe.f2.f0.tokenImage));

        } else if (choice instanceof MinusExpression) {
            MinusExpression me = (MinusExpression) choice;
            emit(constOrId(me.f0.f0.tokenImage) + " - "
                    + constOrId(me.f2.f0.tokenImage));

        } else if (choice instanceof TimesExpression) {
            TimesExpression te = (TimesExpression) choice;
            emit(constOrId(te.f0.f0.tokenImage) + " * "
                    + constOrId(te.f2.f0.tokenImage));

        } else if (choice instanceof ArrayLookup) {
            // f0=arr f2=index(Id)
            ArrayLookup al = (ArrayLookup) choice;
            emit(al.f0.f0.tokenImage + "[" + constOrId(al.f2.f0.tokenImage) + "]");

        } else if (choice instanceof ArrayLength) {
            emit(((ArrayLength) choice).f0.f0.tokenImage + ".length");

        } else if (choice instanceof MessageSend) {
            emitMessageSend((MessageSend) choice);

        } else if (choice instanceof FieldRead) {
            // f0=obj f2=field(Id)
            FieldRead fr = (FieldRead) choice;
            emit(fr.f0.f0.tokenImage + "." + fr.f2.f0.tokenImage);

        } else if (choice instanceof PrimaryExpression) {
            emitPrimary((PrimaryExpression) choice);
        }
    }

    private void emitMessageSend(MessageSend ms) {
        // f0=receiver f2=methodName f4=ArgList?
        emitPrimary(ms.f0);
        emit("." + ms.f2.f0.tokenImage + "(");
        if (ms.f4.present()) {
            ArgList al = (ArgList) ms.f4.node;
            emit(constOrId(al.f0.f0.tokenImage));
            if (al.f1.present()) {
                for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();) {
                    ArgRest ar = (ArgRest) e.nextElement();
                    emit(", " + constOrId(ar.f1.f0.tokenImage));
                }
            }
        }
        emit(")");
    }

    private void emitPrimary(PrimaryExpression pe) {
        Node choice = pe.f0.choice;
        if (choice instanceof IntegerLiteral) {
            emit(((IntegerLiteral) choice).f0.tokenImage);
        } else if (choice instanceof TrueLiteral) {
            emit("true");
        } else if (choice instanceof FalseLiteral) {
            emit("false");
        } else if (choice instanceof Identifier) {
            emit(constOrId(((Identifier) choice).f0.tokenImage));
        } else if (choice instanceof ThisExpression) {
            emit("this");
        } else if (choice instanceof ArrayAllocationExpression) {
            // f3=size(Id)
            emit("new int[" + constOrId(
                    ((ArrayAllocationExpression) choice).f3.f0.tokenImage) + "]");
        } else if (choice instanceof AllocationExpression) {
            emit("new " + ((AllocationExpression) choice).f1.f0.tokenImage + "()");
        } else if (choice instanceof NotExpression) {
            emit("!" + ((NotExpression) choice).f1.f0.tokenImage);
        }
    }

    // ---------------------------------------------------------------
    // VarDeclaration list — emitted via accept (not through emitStatement)
    // ---------------------------------------------------------------
    private void emitVarDeclList(NodeListOptional nlo) {
        if (!nlo.present())
            return;
        for (Enumeration<Node> e = nlo.elements(); e.hasMoreElements();)
            e.nextElement().accept(this, null);
    }

    // ---------------------------------------------------------------
    // Type emitter
    // ---------------------------------------------------------------
    private void emitType(Type t) {
        Node choice = t.f0.choice;
        if (choice instanceof ArrayType)
            emit("int []");
        else if (choice instanceof BooleanType)
            emit("boolean");
        else if (choice instanceof IntegerType)
            emit("int");
        else if (choice instanceof Identifier)
            emit(((Identifier) choice).f0.tokenImage);
    }

    // ---------------------------------------------------------------
    // Constant substitution
    // ---------------------------------------------------------------
    private String constOrId(String varName) {
        if (currentClass == null || currentMethod == null)
            return varName;
        CPValue val = cp.getIdValue(currentClass, currentMethod, varName);
        if (val.isConst())
            return val.toTokenString();
        return varName;
    }

    // ---------------------------------------------------------------
    // Unwrap Statement wrapper to inner concrete node
    // ---------------------------------------------------------------
    private static Node unwrap(Node n) {
        if (n instanceof Statement)
            return ((Statement) n).f0.choice;
        return n;
    }
}