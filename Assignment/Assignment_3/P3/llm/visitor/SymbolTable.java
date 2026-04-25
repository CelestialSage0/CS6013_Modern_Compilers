package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 2: Builds the symbol table.
 *
 * For each class and method, records the declared type of every
 * field, parameter, and local variable. Also records the return
 * type of every method.
 *
 * After visiting the AST call the query methods below to look up types.
 *
 * Key scoping rule used throughout the compiler:
 * Local lookup order: local vars → params → fields of enclosing class
 * → fields of ancestor classes (via hierarchy).
 *
 * Usage:
 * SymbolTableVisitor st = new SymbolTableVisitor(classHierarchy);
 * goal.accept(st, null);
 */
public class SymbolTable extends GJDepthFirst {

  // ---------------------------------------------------------------
  // The class hierarchy (needed to resolve inherited fields)
  // ---------------------------------------------------------------
  private final ClassHierarchy ch;

  // ---------------------------------------------------------------
  // Tables populated during visiting
  // ---------------------------------------------------------------

  /** className -> { fieldName -> typeName } */
  private final Map<String, Map<String, String>> fields = new LinkedHashMap<>();

  /** className -> methodName -> { paramOrLocalName -> typeName } */
  private final Map<String, Map<String, Map<String, String>>> locals = new LinkedHashMap<>();

  /** className -> methodName -> returnType */
  private final Map<String, Map<String, String>> returnTypes = new LinkedHashMap<>();

  /** className -> methodName -> ordered list of parameter names */
  private final Map<String, Map<String, List<String>>> paramOrder = new LinkedHashMap<>();

  // ---------------------------------------------------------------
  // Constructor
  // ---------------------------------------------------------------
  public SymbolTable(ClassHierarchy ch) {
    this.ch = ch;
  }

  // ---------------------------------------------------------------
  // Public query API
  // ---------------------------------------------------------------

  /**
   * Look up the declared type of a variable used inside (className, methodName).
   * Search order: locals/params → fields of className → inherited fields.
   * Returns null if not found.
   */
  public String typeOf(String className, String methodName, String varName) {
    // 1. Locals and params of this method
    Map<String, String> methodLocals = getLocals(className, methodName);
    if (methodLocals != null && methodLocals.containsKey(varName))
      return methodLocals.get(varName);

    // 2. Fields (walk up the hierarchy)
    String cur = className;
    while (cur != null) {
      Map<String, String> f = fields.get(cur);
      if (f != null && f.containsKey(varName))
        return f.get(varName);
      cur = ch.parentOf(cur);
    }
    return null; // unknown
  }

  /**
   * Declared return type of className.methodName.
   */
  public String returnTypeOf(String className, String methodName) {
    Map<String, String> m = returnTypes.get(className);
    return m == null ? null : m.get(methodName);
  }

  /**
   * Ordered list of parameter names for className.methodName.
   */
  public List<String> paramsOf(String className, String methodName) {
    Map<String, List<String>> m = paramOrder.get(className);
    if (m == null)
      return Collections.emptyList();
    List<String> p = m.get(methodName);
    return p == null ? Collections.emptyList() : Collections.unmodifiableList(p);
  }

  /**
   * All field names of className (own fields only, not inherited).
   */
  public Map<String, String> fieldsOf(String className) {
    return fields.getOrDefault(className, Collections.emptyMap());
  }

  /**
   * All locals+params of className.methodName.
   */
  public Map<String, String> localsOf(String className, String methodName) {
    Map<String, String> m = getLocals(className, methodName);
    return m == null ? Collections.emptyMap() : Collections.unmodifiableMap(m);
  }

  // ---------------------------------------------------------------
  // Visitor context tracking
  // ---------------------------------------------------------------

  /** Name of the class currently being visited */
  private String currentClass;

  /** Name of the method currently being visited (null in class body) */
  private String currentMethod;

  // ---------------------------------------------------------------
  // Visitor methods
  // ---------------------------------------------------------------

  /**
   * MainClass: register it; it has no fields or ordinary methods,
   * but we still need to visit VarDeclarations inside main as locals
   * under a synthetic method name "main".
   *
   * f0 -> "class"
   * f1 -> Identifier() <- class name
   * ...
   * f14 -> ( VarDeclaration() )*
   * f15 -> ( Statement() )*
   */
  @Override
  public Object visit(MainClass n, Object argu) {
    currentClass = n.f1.f0.tokenImage;
    currentMethod = "main";
    fields.computeIfAbsent(currentClass, k -> new LinkedHashMap<>());
    ensureMethod(currentClass, currentMethod);
    n.f14.accept(this, argu); // local var declarations inside main
    currentMethod = null;
    currentClass = null;
    return null;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> ( VarDeclaration() )* <- fields
   * f4 -> ( MethodDeclaration() )*
   * f5 -> "}"
   */
  @Override
  public Object visit(ClassDeclaration n, Object argu) {
    currentClass = n.f1.f0.tokenImage;
    currentMethod = null;
    fields.computeIfAbsent(currentClass, k -> new LinkedHashMap<>());
    n.f3.accept(this, argu); // field declarations
    n.f4.accept(this, argu); // method declarations
    currentClass = null;
    return null;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "extends"
   * f3 -> Identifier()
   * f4 -> "{"
   * f5 -> ( VarDeclaration() )* <- fields
   * f6 -> ( MethodDeclaration() )*
   * f7 -> "}"
   */
  @Override
  public Object visit(ClassExtendsDeclaration n, Object argu) {
    currentClass = n.f1.f0.tokenImage;
    currentMethod = null;
    fields.computeIfAbsent(currentClass, k -> new LinkedHashMap<>());
    n.f5.accept(this, argu); // field declarations
    n.f6.accept(this, argu); // method declarations
    currentClass = null;
    return null;
  }

  /**
   * VarDeclaration:
   * - Inside a method (currentMethod != null) → local variable
   * - Inside a class body (currentMethod == null) → field
   *
   * f0 -> Type()
   * f1 -> Identifier()
   * f2 -> ";"
   */
  @Override
  public Object visit(VarDeclaration n, Object argu) {
    String typeName = typeString(n.f0);
    String varName = n.f1.f0.tokenImage;

    if (currentMethod != null) {
      // local variable
      getOrCreateLocals(currentClass, currentMethod).put(varName, typeName);
    } else {
      // field
      fields.computeIfAbsent(currentClass, k -> new LinkedHashMap<>())
          .put(varName, typeName);
    }
    return null;
  }

  /**
   * f0 -> "public"
   * f1 -> Type() <- return type
   * f2 -> Identifier() <- method name
   * f3 -> "("
   * f4 -> ( FormalParameterList() )?
   * f5 -> ")"
   * f6 -> "{"
   * f7 -> ( VarDeclaration() )*
   * f8 -> ( Statement() )*
   * f9 -> "return"
   * f10 -> Identifier()
   * f11 -> ";"
   * f12 -> "}"
   */
  @Override
  public Object visit(MethodDeclaration n, Object argu) {
    currentMethod = n.f2.f0.tokenImage;
    String retType = typeString(n.f1);

    ensureMethod(currentClass, currentMethod);
    returnTypes.computeIfAbsent(currentClass, k -> new LinkedHashMap<>())
        .put(currentMethod, retType);
    paramOrder.computeIfAbsent(currentClass, k -> new LinkedHashMap<>())
        .computeIfAbsent(currentMethod, k -> new ArrayList<>());

    n.f4.accept(this, argu); // formal parameters (if any)
    n.f7.accept(this, argu); // local var declarations

    currentMethod = null;
    return null;
  }

  /**
   * f0 -> Type()
   * f1 -> Identifier()
   */
  @Override
  public Object visit(FormalParameter n, Object argu) {
    String typeName = typeString(n.f0);
    String paramName = n.f1.f0.tokenImage;

    // Params go into the locals map AND the ordered param list
    getOrCreateLocals(currentClass, currentMethod).put(paramName, typeName);
    paramOrder.computeIfAbsent(currentClass, k -> new LinkedHashMap<>())
        .computeIfAbsent(currentMethod, k -> new ArrayList<>())
        .add(paramName);
    return null;
  }

  // ---------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------

  /**
   * Convert a Type AST node to a plain String (e.g., "int", "boolean",
   * "int[]", or a class name).
   */
  private String typeString(Type t) {
    Node choice = t.f0.choice;
    if (choice instanceof ArrayType)
      return "int[]";
    if (choice instanceof BooleanType)
      return "boolean";
    if (choice instanceof IntegerType)
      return "int";
    // Otherwise it's an Identifier node
    return ((Identifier) choice).f0.tokenImage;
  }

  private void ensureMethod(String cls, String method) {
    locals.computeIfAbsent(cls, k -> new LinkedHashMap<>())
        .computeIfAbsent(method, k -> new LinkedHashMap<>());
  }

  private Map<String, String> getLocals(String cls, String method) {
    Map<String, Map<String, String>> m = locals.get(cls);
    return m == null ? null : m.get(method);
  }

  private Map<String, String> getOrCreateLocals(String cls, String method) {
    return locals.computeIfAbsent(cls, k -> new LinkedHashMap<>())
        .computeIfAbsent(method, k -> new LinkedHashMap<>());
  }
}
