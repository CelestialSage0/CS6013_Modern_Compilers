package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 1: Walk the AST and build the symbol table.
 * Populates a Map<String, ClassInfo> with all classes, their fields, and
 * methods.
 *
 * Uses GJDepthFirst<String, Void>:
 * R = String (used to return names / type strings from sub-nodes)
 * A = Void (no argument needed)
 */
public class SymbolTableBuilder extends GJDepthFirst<String, Void> {

  // The symbol table we are building - class name -> ClassInfo
  public Map<String, ClassInfo> classes = new LinkedHashMap<>();

  // ---- state during traversal ----
  private ClassInfo currentClass = null;
  private MethodInfo currentMethod = null;

  // -------------------------------------------------------
  // Helpers
  // -------------------------------------------------------

  /**
   * Return the declared type of field 'field' in class 'cls' (walks hierarchy).
   */
  public String fieldType(String cls, String field) {
    while (cls != null) {
      ClassInfo ci = classes.get(cls);
      if (ci == null)
        return null;
      if (ci.fields.containsKey(field))
        return ci.fields.get(field);
      cls = ci.parent;
    }
    return null;
  }

  /**
   * Return the MethodInfo for method 'method' in class 'cls' (walks hierarchy).
   */
  public MethodInfo lookupMethod(String cls, String method) {
    while (cls != null) {
      ClassInfo ci = classes.get(cls);
      if (ci == null)
        return null;
      if (ci.methods.containsKey(method))
        return ci.methods.get(method);
      cls = ci.parent;
    }
    return null;
  }

  /** True if 'sub' is a subtype of (or equal to) 'sup'. */
  public boolean isSubtype(String sub, String sup) {
    if (sub == null || sup == null)
      return false;
    if (sub.equals(sup))
      return true;
    ClassInfo ci = classes.get(sub);
    if (ci == null || ci.parent == null)
      return false;
    return isSubtype(ci.parent, sup);
  }

  // -------------------------------------------------------
  // Visitor methods
  // -------------------------------------------------------

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> ( VarDeclaration() )*
   * f4 -> ( MethodDeclaration() )*
   * f5 -> "}"
   */
  @Override
  public String visit(ClassDeclaration n, Void argu) {
    String name = n.f1.accept(this, argu);
    currentClass = new ClassInfo(name, null);
    classes.put(name, currentClass);

    n.f3.accept(this, argu); // VarDeclarations -> fields
    n.f4.accept(this, argu); // MethodDeclarations

    currentClass = null;
    return null;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier() (child class)
   * f2 -> "extends"
   * f3 -> Identifier() (parent class)
   * f4 -> "{"
   * f5 -> ( VarDeclaration() )*
   * f6 -> ( MethodDeclaration() )*
   * f7 -> "}"
   */
  @Override
  public String visit(ClassExtendsDeclaration n, Void argu) {
    String name = n.f1.accept(this, argu);
    String parent = n.f3.accept(this, argu);
    currentClass = new ClassInfo(name, parent);
    classes.put(name, currentClass);

    n.f5.accept(this, argu); // fields
    n.f6.accept(this, argu); // methods

    currentClass = null;
    return null;
  }

  /**
   * f0 -> Type()
   * f1 -> Identifier()
   * f2 -> ";"
   *
   * When inside a method -> local variable declaration.
   * When inside a class (not method) -> field declaration.
   */
  @Override
  public String visit(VarDeclaration n, Void argu) {
    String type = n.f0.accept(this, argu);
    String name = n.f1.accept(this, argu);

    if (currentMethod != null) {
      currentMethod.localNames.add(name);
      currentMethod.localTypes.put(name, type);
    } else if (currentClass != null) {
      currentClass.fields.put(name, type);
    }
    return null;
  }

  /**
   * f0 -> "public"
   * f1 -> Type()
   * f2 -> Identifier() (method name)
   * f3 -> "("
   * f4 -> ( FormalParameterList() )?
   * f5 -> ")"
   * f6 -> "{"
   * f7 -> ( VarDeclaration() )*
   * f8 -> ( Statement() )*
   * f9 -> "return"
   * f10 -> ConstOrId()
   * f11 -> ";"
   * f12 -> "}"
   */
  @Override
  public String visit(MethodDeclaration n, Void argu) {
    String retType = n.f1.accept(this, argu);
    String mname = n.f2.accept(this, argu);

    currentMethod = new MethodInfo(mname, retType, currentClass.name);
    currentMethod.astNode = n;

    // formal parameters
    n.f4.accept(this, argu);
    // local variable declarations
    n.f7.accept(this, argu);

    // do NOT recurse into statements - we don't need to build symbol info there
    // (TypeFlowVisitor will handle statements)

    currentClass.methods.put(mname, currentMethod);
    currentMethod = null;
    return null;
  }

  /**
   * f0 -> Type()
   * f1 -> Identifier()
   */
  @Override
  public String visit(FormalParameter n, Void argu) {
    String type = n.f0.accept(this, argu);
    String name = n.f1.accept(this, argu);

    currentMethod.paramNames.add(name);
    currentMethod.paramTypes.put(name, type);
    return null;
  }

  // -------------------------------------------------------
  // Type nodes - return type string
  // -------------------------------------------------------

  /** f0 -> "int" f1 -> "[" f2 -> "]" */
  @Override
  public String visit(ArrayType n, Void argu) {
    return "int[]";
  }

  /** f0 -> "boolean" */
  @Override
  public String visit(BooleanType n, Void argu) {
    return "boolean";
  }

  /** f0 -> "int" */
  @Override
  public String visit(IntegerType n, Void argu) {
    return "int";
  }

  /**
   * Type -> Identifier (class type)
   * f0 -> ArrayType | BooleanType | IntegerType | Identifier
   */
  @Override
  public String visit(Type n, Void argu) {
    return n.f0.accept(this, argu);
  }

  /** f0 -> <IDENTIFIER> */
  @Override
  public String visit(Identifier n, Void argu) {
    return n.f0.tokenImage;
  }
}