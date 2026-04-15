import syntaxtree.*;
import visitor.*;
import java.util.Enumeration;

/**
 * Pass 1 — Symbol Table Visitor.
 *
 * Walks the BuritoJava AST (produced by JTB) and populates a SymbolTable
 * with all class, field, and method information needed by Pass 2.
 *
 * Generic parameters:
 * R = String (return type: used to pass type strings up the call stack)
 * A = String[] (argument: [0] = currentClassName, [1] = currentMethodName)
 *
 * Usage:
 * SymbolTableVisitor stv = new SymbolTableVisitor();
 * root.accept(stv, new String[]{null, null});
 * SymbolTable st = stv.symTable;
 */
public class SymbolTableVisitor extends GJDepthFirst<String, String[]> {

  public final SymbolTable symTable = new SymbolTable();

  // -------------------------------------------------------
  // Top-level structure
  // -------------------------------------------------------

  @Override
  public String visit(Goal n, String[] argu) {
    // MainClass (f0), TypeDeclaration* (f1), EOF (f2)
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    return null;
  }

  /**
   * MainClass: "class" Identifier() "{" "public" "static" "void" "main"
   * "(" "String" "[" "]" Identifier() ")" "{" PrintStatement() "}" "}"
   * f1 = class name Identifier
   */
  @Override
  public String visit(MainClass n, String[] argu) {
    String className = n.f1.f0.tokenImage;
    symTable.addClass(className, null);
    // main has no meaningful method entry for type-lookup purposes,
    // but we register it so CodeGenVisitor can resolve temps inside main.
    SymbolTable.MethodInfo mi = new SymbolTable.MethodInfo("main", "void");
    symTable.getClass(className).addMethod(mi);
    return null;
  }

  /**
   * TypeDeclaration: ClassDeclaration | ClassExtendsDeclaration
   * f0 = NodeChoice (which 0/1)
   */
  @Override
  public String visit(TypeDeclaration n, String[] argu) {
    n.f0.accept(this, argu);
    return null;
  }

  // -------------------------------------------------------
  // Class declarations
  // -------------------------------------------------------

  /**
   * ClassDeclaration: "class" Identifier() "{" (SimpleVarDeclaration)*
   * (MethodDeclaration)* "}"
   * f1=class name, f3=fields*, f4=methods*
   */
  @Override
  public String visit(ClassDeclaration n, String[] argu) {
    String className = n.f1.f0.tokenImage;
    symTable.addClass(className, null);
    String[] ctx = { className, null };
    n.f3.accept(this, ctx); // SimpleVarDeclaration*
    n.f4.accept(this, ctx); // MethodDeclaration*
    return null;
  }

  /**
   * ClassExtendsDeclaration:
   * "class" Identifier() "extends" Identifier() "{" (SimpleVarDeclaration)*
   * (MethodDeclaration)* "}"
   * f1=class name, f3=super name, f5=fields*, f6=methods*
   */
  @Override
  public String visit(ClassExtendsDeclaration n, String[] argu) {
    String className = n.f1.f0.tokenImage;
    String superName = n.f3.f0.tokenImage;
    symTable.addClass(className, superName);
    String[] ctx = { className, null };
    n.f5.accept(this, ctx); // SimpleVarDeclaration*
    n.f6.accept(this, ctx); // MethodDeclaration*
    return null;
  }

  // -------------------------------------------------------
  // Variable declarations
  // -------------------------------------------------------

  /**
   * SimpleVarDeclaration: Type() Identifier() ";"
   * f0=Type, f1=Identifier
   */
  @Override
  public String visit(SimpleVarDeclaration n, String[] argu) {
    String type = n.f0.accept(this, argu); // visit Type, returns type string
    String name = n.f1.f0.tokenImage;
    String cls = argu[0];
    String meth = argu[1];

    if (meth == null) {
      // class-level field
      symTable.getClass(cls).addField(name, type);
    } else {
      // method-level local
      symTable.getClass(cls).getMethod(meth).addLocal(name, type);
    }
    return null;
  }

  /**
   * FinalVarDeclaration: "final" Type() Identifier() "=" Expression() ";"
   * f1=Type, f2=Identifier
   * (Expression is not visited in the ST pass — only the type matters.)
   */
  @Override
  public String visit(FinalVarDeclaration n, String[] argu) {
    String type = n.f1.accept(this, argu);
    String name = n.f2.f0.tokenImage;
    String cls = argu[0];
    String meth = argu[1];

    if (meth == null) {
      symTable.getClass(cls).addField(name, type);
    } else {
      symTable.getClass(cls).getMethod(meth).addLocal(name, type);
    }
    return null;
  }

  /**
   * VarDeclaration: SimpleVarDeclaration | FinalVarDeclaration
   * f0 = NodeChoice
   */
  @Override
  public String visit(VarDeclaration n, String[] argu) {
    n.f0.accept(this, argu);
    return null;
  }

  // -------------------------------------------------------
  // Method declarations
  // -------------------------------------------------------

  /**
   * MethodDeclaration:
   * "public" Type() Identifier() "(" (FormalParameterList)? ")"
   * "{" (VarDeclaration)* (Statement)* "return" Expression() ";" "}"
   * f1=return type, f2=method name, f4=params (optional), f7=locals*
   */
  @Override
  public String visit(MethodDeclaration n, String[] argu) {
    String retType = n.f1.accept(this, argu);
    String methName = n.f2.f0.tokenImage;
    String cls = argu[0];

    SymbolTable.MethodInfo mi = new SymbolTable.MethodInfo(methName, retType);
    symTable.getClass(cls).addMethod(mi);

    String[] ctx = { cls, methName };

    // Formal parameters (optional)
    if (n.f4.present()) {
      n.f4.node.accept(this, ctx);
    }

    // Local variable declarations
    n.f7.accept(this, ctx);

    return null;
  }

  /**
   * FormalParameterList: FormalParameter() (FormalParameterRest())*
   */
  @Override
  public String visit(FormalParameterList n, String[] argu) {
    n.f0.accept(this, argu); // first param
    n.f1.accept(this, argu); // rest*
    return null;
  }

  /**
   * FormalParameter: Type() Identifier()
   */
  @Override
  public String visit(FormalParameter n, String[] argu) {
    String type = n.f0.accept(this, argu);
    String name = n.f1.f0.tokenImage;
    symTable.getClass(argu[0]).getMethod(argu[1]).addParam(type, name);
    return null;
  }

  /**
   * FormalParameterRest: "," FormalParameter()
   */
  @Override
  public String visit(FormalParameterRest n, String[] argu) {
    n.f1.accept(this, argu);
    return null;
  }

  // -------------------------------------------------------
  // Type — returns type string
  // -------------------------------------------------------

  /**
   * Type: ArrayType | BooleanType | IntegerType | Identifier
   * f0 = NodeChoice (which 0..3)
   */
  @Override
  public String visit(Type n, String[] argu) {
    switch (n.f0.which) {
      case 0:
        return "int[]";
      case 1:
        return "boolean";
      case 2:
        return "int";
      case 3:
        return ((Identifier) n.f0.choice).f0.tokenImage;
      default:
        return "Object";
    }
  }

  // -------------------------------------------------------
  // Identifier — returns token image
  // -------------------------------------------------------

  @Override
  public String visit(Identifier n, String[] argu) {
    return n.f0.tokenImage;
  }
}
