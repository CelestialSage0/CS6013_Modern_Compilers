package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 2: Flow-sensitive, context-sensitive points-to analysis.
 *
 * R = String (returns a variable name or type string from expressions)
 * A = Map<String, Set<String>> (current points-to map: varName -> set of
 * concrete types)
 *
 * After this pass, inlineTargets maps each MessageSendStatement node
 * (that has an InlineAnn and is monomorphic) to the concrete class name to
 * inline from.
 *
 * For non-reference variables (int, boolean, int[]) the pt-set is empty (or
 * absent).
 */
public class TypeFlowVisitor extends GJDepthFirst<String, Map<String, Set<String>>> {

  private final ClassBuilderVisitor symtab;

  // OUTPUT: call site node -> concrete class to inline (null entry = not
  // monomorphic / no annotation)
  public final Map<MessageSendStatement, String> inlineTargets = new IdentityHashMap<>();

  public TypeFlowVisitor(ClassBuilderVisitor symtab) {
    this.symtab = symtab;
  }

  // -------------------------------------------------------
  // Helpers
  // -------------------------------------------------------

  private Set<String> ptOf(String var, Map<String, Set<String>> pt) {
    return pt.getOrDefault(var, Collections.emptySet());
  }

  private void setPt(String var, Set<String> types, Map<String, Set<String>> pt) {
    pt.put(var, new HashSet<>(types));
  }

  /** Deep-copy a points-to map */
  private Map<String, Set<String>> copyPt(Map<String, Set<String>> pt) {
    Map<String, Set<String>> copy = new HashMap<>();
    for (Map.Entry<String, Set<String>> e : pt.entrySet()) {
      copy.put(e.getKey(), new HashSet<>(e.getValue()));
    }
    return copy;
  }

  /** Merge (union) src into dst in-place */
  private void mergePt(Map<String, Set<String>> dst, Map<String, Set<String>> src) {
    for (Map.Entry<String, Set<String>> e : src.entrySet()) {
      dst.computeIfAbsent(e.getKey(), k -> new HashSet<>()).addAll(e.getValue());
    }
  }

  /**
   * Resolve the concrete type of the receiver of a MessageSend.
   * The receiver is a PrimaryExpression which in FunkyTACoJava is just an
   * Identifier or "this".
   * Returns the concrete class name if monomorphic, else null.
   */
  private String resolveReceiver(MessageSend ms, Map<String, Set<String>> pt) {
    // f0 -> PrimaryExpression (which is Identifier or "this" in FunkyTACoJava
    // calls)
    PrimaryExpression pe = (PrimaryExpression) ms.f0.f0.choice;
    String receiverName;
    if (pe.f0.choice instanceof Identifier) {
      receiverName = ((Identifier) pe.f0.choice).f0.tokenImage;
    } else if (pe.f0.choice instanceof ThisExpression) {
      receiverName = "this";
    } else {
      return null; // AllocationExpression etc. - shouldn't appear as receiver in FunkyTACoJava
    }

    Set<String> types = ptOf(receiverName, pt);
    if (types.size() == 1) {
      return types.iterator().next();
    }
    return null; // polymorphic or unknown
  }

  /**
   * Get the declared return type of method 'mname' in class 'cls'.
   * Returns null for void or unknown.
   */
  private String returnType(String cls, String mname) {
    MethodInfo mi = symtab.lookupMethod(cls, mname);
    if (mi == null)
      return null;
    return mi.returnType;
  }

  /**
   * Collect declared subclasses of a given class (direct + transitive).
   * Used to find all classes that override a method.
   */
  private Set<String> allSubclasses(String cls) {
    Set<String> result = new HashSet<>();
    for (ClassInfo ci : symtab.classes.values()) {
      if (symtab.isSubtype(ci.name, cls) && !ci.name.equals(cls)) {
        result.add(ci.name);
      }
    }
    return result;
  }

  // -------------------------------------------------------
  // Extract string name from ConstOrId / Identifier nodes
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

  /** Returns the variable/literal name from ConstOrId */
  @Override
  public String visit(ConstOrId n, Map<String, Set<String>> argu) {
    // f0 -> IntegerLiteral | Identifier | TrueLiteral | FalseLiteral
    Node choice = n.f0.choice;
    if (choice instanceof Identifier) {
      return ((Identifier) choice).f0.tokenImage;
    }
    // literals - not a variable, return a sentinel
    return null;
  }

  // -------------------------------------------------------
  // Top-level: Goal, MainClass, TypeDeclaration
  // -------------------------------------------------------

  /**
   * f0 -> MainClass()
   * f1 -> ( TypeDeclaration() )*
   * f2 -> <EOF>
   */
  @Override
  public String visit(Goal n, Map<String, Set<String>> argu) {
    Map<String, Set<String>> pt = new HashMap<>();
    n.f0.accept(this, pt);
    n.f1.accept(this, pt);
    return null;
  }

  /**
   * MainClass: analyze main method body
   * f14 -> ( VarDeclaration() )*
   * f15 -> ( Statement() )*
   */
  @Override
  public String visit(MainClass n, Map<String, Set<String>> argu) {
    // fresh pt map for main
    Map<String, Set<String>> pt = new HashMap<>();
    n.f15.accept(this, pt);
    return null;
  }

  /**
   * TypeDeclaration -> ClassDeclaration | ClassExtendsDeclaration
   * Each class's methods are analyzed when called (context-sensitive).
   * We do NOT analyze method bodies here; instead we analyze them
   * at call sites (context-sensitive inlining analysis).
   */
  @Override
  public String visit(TypeDeclaration n, Map<String, Set<String>> argu) {
    // Do not recursively analyze class bodies top-down.
    // Methods will be analyzed on demand from call sites.
    return null;
  }

  // -------------------------------------------------------
  // Statement visitors
  // -------------------------------------------------------

  /**
   * Statement dispatches to its choice.
   * f0 -> Block | AssignmentStatement | ArrayAssignmentStatement |
   * FieldAssignmentStatement | IfStatement | WhileStatement |
   * ForStatement | PrintStatement | MessageSendStatement
   */
  @Override
  public String visit(Statement n, Map<String, Set<String>> argu) {
    n.f0.accept(this, argu);
    return null;
  }

  /** f0-f2: { (Statement)* } */
  @Override
  public String visit(Block n, Map<String, Set<String>> argu) {
    n.f1.accept(this, argu);
    return null;
  }

  /**
   * AssignmentStatement: Identifier "=" RhsExpression ";"
   * The RHS can be:
   * - DotExpression: x.f -> pt(lhs) = declared type of field f in pt(x)
   * - AllocationExpression: new Foo() -> pt(lhs) = {Foo}
   * - Identifier (another var): pt(lhs) = pt(rhs)
   * - Arithmetic / boolean: pt(lhs) = {} (not a reference)
   */
  @Override
  public String visit(AssignmentStatement n, Map<String, Set<String>> argu) {
    String lhs = n.f0.f0.tokenImage;
    // visit RHS to get a "type hint" string - we'll handle each case
    analyzeRhs(lhs, n.f2, argu);
    return null;
  }

  /**
   * Handle RhsExpression and update pt[lhs] accordingly.
   */
  private void analyzeRhs(String lhs, RhsExpression rhs, Map<String, Set<String>> pt) {
    Node choice = rhs.f0.choice;

    if (choice instanceof DotExpression) {
      // x.f -> look up declared type of field f in whatever type x has
      DotExpression de = (DotExpression) choice;
      String obj = de.f0.f0.tokenImage;
      String field = de.f2.f0.tokenImage;
      Set<String> objTypes = ptOf(obj, pt);
      Set<String> result = new HashSet<>();
      for (String cls : objTypes) {
        String ft = symtab.fieldType(cls, field);
        if (ft != null && isReferenceType(ft)) {
          result.add(ft); // declared type - overapproximation
        }
      }
      setPt(lhs, result, pt);

    } else {
      // Expression branch
      Expression expr = (Expression) choice;
      analyzeExpression(lhs, expr, pt);
    }
  }

  private void analyzeExpression(String lhs, Expression expr, Map<String, Set<String>> pt) {
    Node choice = expr.f0.choice;

    if (choice instanceof AllocationExpression) {
      // new Foo() -> pt(lhs) = {Foo}
      AllocationExpression ae = (AllocationExpression) choice;
      String cls = ae.f1.f0.tokenImage;
      Set<String> s = new HashSet<>();
      s.add(cls);
      setPt(lhs, s, pt);

    } else if (choice instanceof PrimaryExpression) {
      PrimaryExpression pe = (PrimaryExpression) choice;
      analyzePrimary(lhs, pe, pt);

    } else {
      // Arithmetic, comparison, array lookup, etc. -> not a reference
      setPt(lhs, new HashSet<>(), pt);
    }
  }

  private void analyzePrimary(String lhs, PrimaryExpression pe, Map<String, Set<String>> pt) {
    Node choice = pe.f0.choice;
    if (choice instanceof Identifier) {
      String src = ((Identifier) choice).f0.tokenImage;
      // copy pt
      setPt(lhs, new HashSet<>(ptOf(src, pt)), pt);
    } else if (choice instanceof ThisExpression) {
      setPt(lhs, new HashSet<>(ptOf("this", pt)), pt);
    } else if (choice instanceof AllocationExpression) {
      AllocationExpression ae = (AllocationExpression) choice;
      String cls = ae.f1.f0.tokenImage;
      Set<String> s = new HashSet<>();
      s.add(cls);
      setPt(lhs, s, pt);
    } else {
      // literals, array alloc, not-expression - not a reference
      setPt(lhs, new HashSet<>(), pt);
    }
  }

  /** ArrayAssignmentStatement: arr[i] = v; - no pt update for reference vars */
  @Override
  public String visit(ArrayAssignmentStatement n, Map<String, Set<String>> argu) {
    return null;
  }

  /** FieldAssignmentStatement: obj.f = v; - no pt update needed for analysis */
  @Override
  public String visit(FieldAssignmentStatement n, Map<String, Set<String>> argu) {
    return null;
  }

  /**
   * IfStatement: if (id) S1 else S2
   * Analyze both branches with copies of current pt, then merge.
   */
  @Override
  public String visit(IfStatement n, Map<String, Set<String>> argu) {
    Map<String, Set<String>> ptThen = copyPt(argu);
    Map<String, Set<String>> ptElse = copyPt(argu);

    n.f4.accept(this, ptThen); // then branch
    n.f6.accept(this, ptElse); // else branch

    // merge both back into argu
    argu.clear();
    mergePt(argu, ptThen);
    mergePt(argu, ptElse);
    return null;
  }

  /**
   * WhileStatement: while (id) S
   * Approximate: analyze body once, merge with pre-state.
   */
  @Override
  public String visit(WhileStatement n, Map<String, Set<String>> argu) {
    Map<String, Set<String>> ptBody = copyPt(argu);
    n.f4.accept(this, ptBody);
    mergePt(argu, ptBody);
    return null;
  }

  /**
   * ForStatement: for (id = expr; expr; id = expr) S
   */
  @Override
  public String visit(ForStatement n, Map<String, Set<String>> argu) {
    Map<String, Set<String>> ptBody = copyPt(argu);
    n.f12.accept(this, ptBody);
    mergePt(argu, ptBody);
    return null;
  }

  /** PrintStatement: no pt change */
  @Override
  public String visit(PrintStatement n, Map<String, Set<String>> argu) {
    return null;
  }

  /**
   * MessageSendStatement:
   * f0 -> ( InlineAnn() )?
   * f1 -> ( VoidMessageSendStmt() | RetMessageSendStmt() )
   *
   * This is where we:
   * 1. Check if INLINE annotated
   * 2. Resolve the receiver's concrete type
   * 3. If monomorphic, record in inlineTargets and analyze the callee body
   * with a fresh context (context-sensitive)
   * 4. Update pt[lhs] with the return type info
   */
  @Override
  public String visit(MessageSendStatement n, Map<String, Set<String>> argu) {
    boolean hasInline = n.f0.present();

    // Extract the MessageSend node and optional LHS
    MessageSend ms;
    String lhs = null;
    Node stmtChoice = n.f1.f0.choice;
    if (stmtChoice instanceof RetMessageSendStmt) {
      RetMessageSendStmt rms = (RetMessageSendStmt) stmtChoice;
      lhs = rms.f0.f0.tokenImage;
      ms = rms.f2;
    } else {
      VoidMessageSendStmt vms = (VoidMessageSendStmt) stmtChoice;
      ms = vms.f0;
    }

    // Method name being called
    String methodName = ms.f2.f0.tokenImage;

    // Resolve receiver concrete type
    String concreteClass = resolveReceiver(ms, argu);

    if (hasInline && concreteClass != null) {
      // Monomorphic INLINE call - record it
      inlineTargets.put(n, concreteClass);

      // Context-sensitive analysis: analyze the callee body with
      // a fresh pt map derived from the call site context
      MethodInfo mi = symtab.lookupMethod(concreteClass, methodName);
      if (mi != null) {
        Map<String, Set<String>> calleePt = buildCalleePt(ms, mi, concreteClass, argu);
        // Analyze callee statements with this context
        mi.astNode.f8.accept(this, calleePt);
        // (results of inner inlineTargets are recorded globally)
      }
    }

    // Update pt[lhs] with return type (conservative)
    if (lhs != null) {
      String retType = null;
      if (concreteClass != null) {
        retType = returnType(concreteClass, methodName);
      } else {
        // Try to find from declared type of receiver
        retType = guessReturnType(ms, methodName, argu);
      }
      if (retType != null && isReferenceType(retType)) {
        Set<String> s = new HashSet<>();
        s.add(retType);
        setPt(lhs, s, argu);
      } else {
        setPt(lhs, new HashSet<>(), argu);
      }
    }

    return null;
  }

  /**
   * Build the initial points-to map for the callee method body.
   * Maps "this" -> {concreteClass}, and each param name -> pt(actual arg).
   */
  private Map<String, Set<String>> buildCalleePt(
      MessageSend ms, MethodInfo mi, String concreteClass,
      Map<String, Set<String>> callerPt) {

    Map<String, Set<String>> calleePt = new HashMap<>();

    // "this" -> concreteClass
    Set<String> thisSet = new HashSet<>();
    thisSet.add(concreteClass);
    calleePt.put("this", thisSet);

    // Map formal params -> pt of actual args
    List<String> args = extractArgs(ms);
    for (int i = 0; i < mi.paramNames.size() && i < args.size(); i++) {
      String paramName = mi.paramNames.get(i);
      String actualArg = args.get(i);
      if (actualArg != null) {
        calleePt.put(paramName, new HashSet<>(ptOf(actualArg, callerPt)));
      }
    }

    return calleePt;
  }

  /**
   * Extract actual argument names/literals from a MessageSend's ArgList.
   * Returns list of variable names (or null for literals).
   */
  private List<String> extractArgs(MessageSend ms) {
    List<String> args = new ArrayList<>();
    if (!ms.f4.present())
      return args;

    ArgList al = (ArgList) ms.f4.node;
    // f0 -> ConstOrId (first arg)
    String first = constOrIdName(al.f0);
    args.add(first);

    // f1 -> ( ArgRest() )*
    NodeListOptional rest = al.f1;
    if (rest.present()) {
      for (Enumeration<Node> e = rest.elements(); e.hasMoreElements();) {
        ArgRest ar = (ArgRest) e.nextElement();
        args.add(constOrIdName(ar.f1));
      }
    }
    return args;
  }

  private String constOrIdName(ConstOrId cid) {
    Node choice = cid.f0.choice;
    if (choice instanceof Identifier)
      return ((Identifier) choice).f0.tokenImage;
    return null; // literal
  }

  /**
   * Try to guess return type when receiver is not monomorphic.
   * Look at all possible classes in the receiver's pt-set.
   */
  private String guessReturnType(MessageSend ms, String methodName,
      Map<String, Set<String>> pt) {
    PrimaryExpression pe = (PrimaryExpression) ms.f0.f0.choice;
    String receiverName = null;
    if (pe.f0.choice instanceof Identifier) {
      receiverName = ((Identifier) pe.f0.choice).f0.tokenImage;
    } else if (pe.f0.choice instanceof ThisExpression) {
      receiverName = "this";
    }
    if (receiverName == null)
      return null;

    Set<String> types = ptOf(receiverName, pt);
    for (String cls : types) {
      MethodInfo mi = symtab.lookupMethod(cls, methodName);
      if (mi != null)
        return mi.returnType;
    }
    return null;
  }

  private boolean isReferenceType(String type) {
    return !type.equals("int") && !type.equals("boolean") && !type.equals("int[]");
  }
}
