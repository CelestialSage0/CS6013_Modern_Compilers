package visitor;

import syntaxtree.*;
import java.io.PrintStream;
import java.util.*;

/**
 * Pass 3: Pretty-print the AST back to FunkyTACoJava, inlining
 * call sites that were identified as monomorphic in Pass 2.
 *
 * All AST dispatch is done via the visitor pattern — no instanceof chains.
 *
 * ExprPrinter (inner GJDepthFirst<String,Void>) handles all expression/type
 * nodes by returning their string representation.
 *
 * PtUpdater (inner GJDepthFirst<Void,String>) updates ptStack from the RHS
 * of AssignmentStatements without instanceof.
 *
 * ReceiverResolver (inner GJDepthFirst<String,Void>) looks up the concrete
 * type of a call receiver from ptStack without instanceof.
 *
 * ptStack (varName -> concrete type) is carried through inlined bodies so
 * nested INLINE sites are resolved per call-site, avoiding the aliasing bug
 * that occurs when two call sites inline the same method (same AST node).
 */
public class InlineTransformVisitor extends GJDepthFirst<Void, Void> {

  private final PrintStream out;
  private final SymbolTableBuilder symtab;
  private final Map<MessageSendStatement, String> inlineTargets;

  private int inlineCounter = 0;
  private int indent = 0;
  private static final String IND = "    ";

  // Per-method state
  private StringBuilder stmtBuffer = null;
  private List<String> extraDecls = null;
  private boolean bufferingMode = false;

  // Renaming and points-to maps, swapped on inlining entry/exit
  private Map<String, String> renaming = new HashMap<>();
  private Map<String, String> ptStack = new HashMap<>();

  // Inner visitors (instantiated once, reused)
  private final ExprPrinter exprPrinter = new ExprPrinter();
  private final PtUpdater ptUpdater = new PtUpdater();
  private final ReceiverResolver receiverResolver = new ReceiverResolver();
  private final OrigNameExtractor origNameExtractor = new OrigNameExtractor();

  public InlineTransformVisitor(PrintStream out,
      SymbolTableBuilder symtab,
      Map<MessageSendStatement, String> inlineTargets) {
    this.out = out;
    this.symtab = symtab;
    this.inlineTargets = inlineTargets;
  }

  // -------------------------------------------------------
  // Output helpers
  // -------------------------------------------------------

  private String ind() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < indent; i++)
      sb.append(IND);
    return sb.toString();
  }

  private void println(String s) {
    out.println(s);
  }

  private void emit(String s) {
    if (bufferingMode)
      stmtBuffer.append(s);
    else
      out.print(s);
  }

  private void emitln(String s) {
    emit(s + "\n");
  }

  private String rename(String v) {
    return renaming.getOrDefault(v, v);
  }

  // Convenience wrappers — delegates to inner visitors
  private String str(Type t) {
    return exprPrinter.visit(t, null);
  }

  private String str(ConstOrId c) {
    return exprPrinter.visit(c, null);
  }

  private String str(RhsExpression r) {
    return exprPrinter.visit(r, null);
  }

  private String str(PrimaryExpression p) {
    return exprPrinter.visit(p, null);
  }

  private String str(Expression e) {
    return exprPrinter.visit(e, null);
  }

  // -------------------------------------------------------
  // ExprPrinter: returns String representation of expressions
  // Uses the outer class's current renaming map via rename().
  // -------------------------------------------------------
  private class ExprPrinter extends GJDepthFirst<String, Void> {

    @Override
    public String visit(ArrayType n, Void a) {
      return "int[]";
    }

    @Override
    public String visit(BooleanType n, Void a) {
      return "boolean";
    }

    @Override
    public String visit(IntegerType n, Void a) {
      return "int";
    }

    @Override
    public String visit(Type n, Void a) {
      return n.f0.accept(this, a);
    }

    @Override
    public String visit(Identifier n, Void a) {
      return rename(n.f0.tokenImage);
    }

    @Override
    public String visit(TrueLiteral n, Void a) {
      return "true";
    }

    @Override
    public String visit(FalseLiteral n, Void a) {
      return "false";
    }

    @Override
    public String visit(PlainIntegerLiteral n, Void a) {
      return n.f0.tokenImage;
    }

    @Override
    public String visit(IntegerLiteralWithPosSign n, Void a) {
      return "+" + n.f1.tokenImage;
    }

    @Override
    public String visit(IntegerLiteralWithNegSign n, Void a) {
      return "-" + n.f1.tokenImage;
    }

    @Override
    public String visit(IntegerLiteral n, Void a) {
      return n.f0.accept(this, a);
    }

    @Override
    public String visit(ConstOrId n, Void a) {
      return n.f0.accept(this, a);
    }

    @Override
    public String visit(ThisExpression n, Void a) {
      return rename("this");
    }

    @Override
    public String visit(AllocationExpression n, Void a) {
      return "new " + n.f1.f0.tokenImage + "()";
    }

    @Override
    public String visit(ArrayAllocationExpression n, Void a) {
      return "new int[" + n.f3.accept(this, a) + "]";
    }

    @Override
    public String visit(NotExpression n, Void a) {
      return "!" + rename(n.f1.f0.tokenImage);
    }

    @Override
    public String visit(PrimaryExpression n, Void a) {
      return n.f0.accept(this, a);
    }

    @Override
    public String visit(AndExpression n, Void a) {
      return n.f0.accept(this, a) + " & " + n.f2.accept(this, a);
    }

    @Override
    public String visit(CompareExpression n, Void a) {
      return n.f0.accept(this, a) + " < " + n.f2.accept(this, a);
    }

    @Override
    public String visit(PlusExpression n, Void a) {
      return n.f0.accept(this, a) + " + " + n.f2.accept(this, a);
    }

    @Override
    public String visit(MinusExpression n, Void a) {
      return n.f0.accept(this, a) + " - " + n.f2.accept(this, a);
    }

    @Override
    public String visit(TimesExpression n, Void a) {
      return n.f0.accept(this, a) + " * " + n.f2.accept(this, a);
    }

    @Override
    public String visit(ArrayLookup n, Void a) {
      return rename(n.f0.f0.tokenImage) + "[" + n.f2.accept(this, a) + "]";
    }

    @Override
    public String visit(ArrayLength n, Void a) {
      return rename(n.f0.f0.tokenImage) + ".length";
    }

    @Override
    public String visit(DotExpression n, Void a) {
      return rename(n.f0.f0.tokenImage) + "." + n.f2.f0.tokenImage;
    }

    @Override
    public String visit(RhsExpression n, Void a) {
      return n.f0.accept(this, a);
    }

    @Override
    public String visit(Expression n, Void a) {
      return n.f0.accept(this, a);
    }
  }

  // -------------------------------------------------------
  // PtUpdater: updates ptStack from AssignmentStatement RHS
  // A = lhs variable name (already renamed)
  // -------------------------------------------------------
  private class PtUpdater extends GJDepthFirst<Void, String> {
    @Override
    public Void visit(AllocationExpression n, String lhs) {
      ptStack.put(lhs, n.f1.f0.tokenImage);
      return null;
    }

    @Override
    public Void visit(Identifier n, String lhs) {
      String type = ptStack.get(rename(n.f0.tokenImage));
      if (type != null)
        ptStack.put(lhs, type);
      return null;
    }

    @Override
    public Void visit(ThisExpression n, String lhs) {
      String type = ptStack.get(rename("this"));
      if (type != null)
        ptStack.put(lhs, type);
      return null;
    }

    @Override
    public Void visit(PrimaryExpression n, String lhs) {
      n.f0.accept(this, lhs);
      return null;
    }

    @Override
    public Void visit(Expression n, String lhs) {
      n.f0.accept(this, lhs);
      return null;
    }

    @Override
    public Void visit(RhsExpression n, String lhs) {
      n.f0.accept(this, lhs);
      return null;
    }
    // Arithmetic, DotExpression etc. fall through to GJDepthFirst no-op
  }

  // -------------------------------------------------------
  // ReceiverResolver: looks up concrete type of receiver via ptStack
  // -------------------------------------------------------
  private class ReceiverResolver extends GJDepthFirst<String, Void> {
    @Override
    public String visit(Identifier n, Void a) {
      return ptStack.get(rename(n.f0.tokenImage));
    }

    @Override
    public String visit(ThisExpression n, Void a) {
      return ptStack.get(rename("this"));
    }

    @Override
    public String visit(PrimaryExpression n, Void a) {
      return n.f0.accept(this, a);
    }
  }

  private String resolveReceiver(MessageSend ms) {
    return ms.f0.accept(receiverResolver, null);
  }

  // -------------------------------------------------------
  // OrigNameExtractor: gets raw identifier name from ConstOrId
  // -------------------------------------------------------
  private class OrigNameExtractor extends GJDepthFirst<String, Void> {
    @Override
    public String visit(Identifier n, Void a) {
      return n.f0.tokenImage;
    }

    @Override
    public String visit(ConstOrId n, Void a) {
      return n.f0.accept(this, a);
    }
    // Literals return null via GJDepthFirst default
  }

  private String origName(ConstOrId c) {
    return origNameExtractor.visit(c, null);
  }

  // -------------------------------------------------------
  // Top-level structure
  // -------------------------------------------------------

  @Override
  public Void visit(Goal n, Void argu) {
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    return null;
  }

  @Override
  public Void visit(MainClass n, Void argu) {
    println("class " + n.f1.f0.tokenImage + " {");
    indent++;
    println(ind() + "public static void main (String[] " + n.f11.f0.tokenImage + ") {");
    indent++;
    emitMethodBody(n.f14, n.f15, null);
    indent--;
    println(ind() + "}");
    indent--;
    println("}");
    return null;
  }

  @Override
  public Void visit(TypeDeclaration n, Void argu) {
    n.f0.accept(this, argu);
    return null;
  }

  @Override
  public Void visit(ClassDeclaration n, Void argu) {
    println("class " + n.f1.f0.tokenImage + " {");
    indent++;
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    indent--;
    println("}");
    return null;
  }

  @Override
  public Void visit(ClassExtendsDeclaration n, Void argu) {
    println("class " + n.f1.f0.tokenImage + " extends " + n.f3.f0.tokenImage + " {");
    indent++;
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    indent--;
    println("}");
    return null;
  }

  @Override
  public Void visit(VarDeclaration n, Void argu) {
    println(ind() + str(n.f0) + " " + n.f1.f0.tokenImage + ";");
    return null;
  }

  @Override
  public Void visit(MethodDeclaration n, Void argu) {
    StringBuilder params = new StringBuilder();
    if (n.f4.present()) {
      FormalParameterList fpl = (FormalParameterList) n.f4.node;
      params.append(str(fpl.f0.f0)).append(" ").append(fpl.f0.f1.f0.tokenImage);
      if (fpl.f1.present())
        for (Enumeration<Node> e = fpl.f1.elements(); e.hasMoreElements();) {
          FormalParameter fp = ((FormalParameterRest) e.nextElement()).f1;
          params.append(", ").append(str(fp.f0)).append(" ").append(fp.f1.f0.tokenImage);
        }
    }
    println(ind() + "public " + str(n.f1) + " " + n.f2.f0.tokenImage + "(" + params + ") {");
    indent++;
    emitMethodBody(n.f7, n.f8, str(n.f10));
    indent--;
    println(ind() + "}");
    return null;
  }

  // -------------------------------------------------------
  // Two-pass method body emission
  // -------------------------------------------------------

  private void emitMethodBody(NodeListOptional varDecls, NodeListOptional stmts, String retVal) {
    List<String> outerExtraDecls = extraDecls;
    StringBuilder outerStmtBuffer = stmtBuffer;
    Map<String, String> outerRenaming = renaming;
    Map<String, String> outerPtStack = ptStack;

    extraDecls = new ArrayList<>();
    stmtBuffer = new StringBuilder();
    renaming = new HashMap<>();
    ptStack = new HashMap<>();

    // Pass 1: buffer statements (also populates extraDecls)
    bufferingMode = true;
    if (stmts.present())
      for (Enumeration<Node> e = stmts.elements(); e.hasMoreElements();)
        ((Statement) e.nextElement()).accept(this, null);
    bufferingMode = false;

    // Emit original var declarations
    if (varDecls.present())
      for (Enumeration<Node> e = varDecls.elements(); e.hasMoreElements();) {
        VarDeclaration vd = (VarDeclaration) e.nextElement();
        out.println(ind() + str(vd.f0) + " " + vd.f1.f0.tokenImage + ";");
      }
    // Emit extra declarations from inlined calls
    for (String decl : extraDecls)
      out.println(ind() + decl + ";");

    out.print(stmtBuffer.toString());

    if (retVal != null)
      out.println(ind() + "return " + retVal + ";");

    extraDecls = outerExtraDecls;
    stmtBuffer = outerStmtBuffer;
    renaming = outerRenaming;
    ptStack = outerPtStack;
  }

  // -------------------------------------------------------
  // Statement visitors
  // -------------------------------------------------------

  @Override
  public Void visit(Statement n, Void argu) {
    n.f0.accept(this, argu);
    return null;
  }

  @Override
  public Void visit(Block n, Void argu) {
    emitln(ind() + "{");
    indent++;
    n.f1.accept(this, argu);
    indent--;
    emitln(ind() + "}");
    return null;
  }

  @Override
  public Void visit(AssignmentStatement n, Void argu) {
    String lhs = rename(n.f0.f0.tokenImage);
    emitln(ind() + lhs + " = " + str(n.f2) + ";");
    // Update ptStack via visitor dispatch — no instanceof
    n.f2.accept(ptUpdater, lhs);
    return null;
  }

  @Override
  public Void visit(ArrayAssignmentStatement n, Void argu) {
    emitln(ind() + rename(n.f0.f0.tokenImage)
        + "[" + str(n.f2) + "] = " + str(n.f5) + ";");
    return null;
  }

  @Override
  public Void visit(FieldAssignmentStatement n, Void argu) {
    emitln(ind() + rename(n.f0.f0.tokenImage)
        + "." + n.f2.f0.tokenImage + " = " + str(n.f4) + ";");
    return null;
  }

  @Override
  public Void visit(IfStatement n, Void argu) {
    emitln(ind() + "if (" + rename(n.f2.f0.tokenImage) + ")");
    indent++;
    n.f4.accept(this, argu);
    indent--;
    emitln(ind() + "else");
    indent++;
    n.f6.accept(this, argu);
    indent--;
    return null;
  }

  @Override
  public Void visit(WhileStatement n, Void argu) {
    emitln(ind() + "while (" + rename(n.f2.f0.tokenImage) + ")");
    indent++;
    n.f4.accept(this, argu);
    indent--;
    return null;
  }

  @Override
  public Void visit(ForStatement n, Void argu) {
    emitln(ind() + "for (" + rename(n.f2.f0.tokenImage)
        + " = " + str(n.f4) + "; " + str(n.f6)
        + "; " + rename(n.f8.f0.tokenImage) + " = " + str(n.f10) + ")");
    indent++;
    n.f12.accept(this, argu);
    indent--;
    return null;
  }

  @Override
  public Void visit(PrintStatement n, Void argu) {
    emitln(ind() + "System.out.println (" + str(n.f2) + ");");
    return null;
  }

  /**
   * MessageSendStatement:
   * f0 -> (InlineAnn)?
   * f1 -> NodeChoice(VoidMessageSendStmt | RetMessageSendStmt)
   *
   * Top-level INLINE: resolved via inlineTargets (TypeFlowVisitor).
   * Nested INLINE (inside inlined body): resolved via ptStack per call-site,
   * bypassing inlineTargets to avoid the shared-AST-node aliasing bug.
   */
  @Override
  public Void visit(MessageSendStatement n, Void argu) {
    boolean hasInline = n.f0.present();

    MessageSend ms;
    String lhs = null;
    Node stmtChoice = n.f1.choice;
    if (stmtChoice instanceof RetMessageSendStmt) {
      RetMessageSendStmt rms = (RetMessageSendStmt) stmtChoice;
      lhs = rename(rms.f0.f0.tokenImage);
      ms = rms.f2;
    } else {
      ms = ((VoidMessageSendStmt) stmtChoice).f0;
    }

    String concreteClass = null;
    if (hasInline)
      concreteClass = renaming.isEmpty()
          ? inlineTargets.get(n) // top-level: use TypeFlowVisitor result
          : resolveReceiver(ms); // nested: resolve from ptStack

    if (concreteClass != null)
      emitInlinedCall(ms, lhs, concreteClass);
    else
      emitRegularCall(ms, lhs);

    return null;
  }

  // -------------------------------------------------------
  // Call emission helpers
  // -------------------------------------------------------

  private void emitRegularCall(MessageSend ms, String lhs) {
    String call = str(ms.f0) + "." + ms.f2.f0.tokenImage + "(" + argsStr(ms) + ")";
    if (lhs != null)
      emitln(ind() + lhs + " = " + call + ";");
    else
      emitln(ind() + call + ";");
  }

  private String argsStr(MessageSend ms) {
    if (!ms.f4.present())
      return "";
    ArgList al = (ArgList) ms.f4.node;
    StringBuilder sb = new StringBuilder(str(al.f0));
    if (al.f1.present())
      for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();)
        sb.append(", ").append(str(((ArgRest) e.nextElement()).f1));
    return sb.toString();
  }

  private void emitInlinedCall(MessageSend ms, String lhs, String concreteClass) {
    String methodName = ms.f2.f0.tokenImage;
    MethodInfo mi = symtab.lookupMethod(concreteClass, methodName);
    if (mi == null) {
      emitRegularCall(ms, lhs);
      return;
    }
    emitln(ind() + "{");
    indent++;

    System.err.println(concreteClass + " : " + methodName);
    int myCounter = inlineCounter++;
    String prefix = "_il" + myCounter + "_";
    String thisVar = prefix + "this";

    String receiverStr = str(ms.f0);
    List<String> actualArgs = extractArgStrs(ms);
    List<String> actualArgOrig = extractArgOrigNames(ms);

    // Register extra declarations
    extraDecls.add(concreteClass + " " + thisVar);
    for (String p : mi.paramNames)
      extraDecls.add(mi.paramTypes.get(p) + " " + prefix + p);
    for (String l : mi.localNames)
      extraDecls.add(mi.localTypes.get(l) + " " + prefix + l);

    // Build callee renaming
    Map<String, String> calleeRenaming = new HashMap<>(renaming);
    calleeRenaming.put("this", thisVar);
    for (String p : mi.paramNames)
      calleeRenaming.put(p, prefix + p);
    for (String l : mi.localNames)
      calleeRenaming.put(l, prefix + l);

    // Build callee ptStack
    Map<String, String> calleePtStack = new HashMap<>(ptStack);
    calleePtStack.put(thisVar, concreteClass);
    for (int i = 0; i < mi.paramNames.size() && i < actualArgOrig.size(); i++) {
      String orig = actualArgOrig.get(i);
      if (orig != null) {
        String argType = ptStack.get(rename(orig));
        if (argType != null)
          calleePtStack.put(prefix + mi.paramNames.get(i), argType);
      }
    }

    // Emit initializations
    emitln(ind() + thisVar + " = " + receiverStr + ";");
    for (int i = 0; i < mi.paramNames.size() && i < actualArgs.size(); i++)
      emitln(ind() + prefix + mi.paramNames.get(i) + " = " + actualArgs.get(i) + ";");

    // Swap context and emit callee body
    Map<String, String> outerRenaming = renaming;
    Map<String, String> outerPtStack = ptStack;
    renaming = calleeRenaming;
    ptStack = calleePtStack;

    MethodDeclaration calleeMD = mi.astNode;
    if (calleeMD.f8.present())
      for (Enumeration<Node> e = calleeMD.f8.elements(); e.hasMoreElements();)
        ((Statement) e.nextElement()).accept(this, null);

    // Emit return assignment using callee renaming
    if (lhs != null) {
      String retExpr = str(calleeMD.f10); // renaming is still calleeRenaming here
      emitln(ind() + lhs + " = " + retExpr + ";");
    }
    indent--;
    emitln(ind() + "}");
    renaming = outerRenaming;
    ptStack = outerPtStack;
  }

  private List<String> extractArgStrs(MessageSend ms) {
    List<String> args = new ArrayList<>();
    if (!ms.f4.present())
      return args;
    ArgList al = (ArgList) ms.f4.node;
    args.add(str(al.f0));
    if (al.f1.present())
      for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();)
        args.add(str(((ArgRest) e.nextElement()).f1));
    return args;
  }

  private List<String> extractArgOrigNames(MessageSend ms) {
    List<String> args = new ArrayList<>();
    if (!ms.f4.present())
      return args;
    ArgList al = (ArgList) ms.f4.node;
    args.add(origName(al.f0));
    if (al.f1.present())
      for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();)
        args.add(origName(((ArgRest) e.nextElement()).f1));
    return args;
  }
}