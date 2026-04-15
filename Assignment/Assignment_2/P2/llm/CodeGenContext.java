import java.util.ArrayList;
import java.util.List;

/**
 * CodeGenContext — mutable state carried through Pass 2 (CodeGenVisitor).
 *
 * Design: TacoJava requires that ALL variable declarations appear before
 * statements inside a method body:
 *
 * { VarDecl* Statement* return Identifier; }
 *
 * To satisfy this, we maintain two separate buffers per method scope:
 * • varDecls — original Type Identifier; declarations
 * • tempDecls — generated _tN declarations (from expression normalization)
 * • bodyStmts — all statements (original + assignments from final vars)
 *
 * At the end of a method scope, we flush: varDecls + tempDecls + bodyStmts.
 *
 * Outside a method scope (class-level, global), everything goes to
 * globalOutput.
 */
public class CodeGenContext {

  // -------------------------------------------------------
  // Global output — class skeletons, method signatures, etc.
  // -------------------------------------------------------
  public final StringBuilder globalOutput = new StringBuilder();

  // -------------------------------------------------------
  // Per-method-scope buffers (null when not in a method)
  // -------------------------------------------------------
  public List<String> varDecls; // "Type name;" — original declarations
  public List<String> tempDecls; // "Type _tN;" — introduced temporaries
  public StringBuilder bodyStmts; // all statements in emission order

  // -------------------------------------------------------
  // Scope state
  // -------------------------------------------------------
  public String currentClass;
  public String currentMethod;
  public int tempCount = 0; // global across all methods (simpler, unique names)
  public int indentLevel = 0; // tracks nesting inside bodyStmts

  // -------------------------------------------------------
  // Symbol table (read-only in Pass 2)
  // -------------------------------------------------------
  public final SymbolTable symTable;

  public CodeGenContext(SymbolTable st) {
    this.symTable = st;
  }

  // -------------------------------------------------------
  // Scope management
  // -------------------------------------------------------

  /**
   * Enter a new method (or main) body scope.
   * Resets the per-method buffers and sets the current context.
   */
  public void enterMethodScope(String className, String methodName) {
    this.currentClass = className;
    this.currentMethod = methodName;
    this.varDecls = new ArrayList<>();
    this.tempDecls = new ArrayList<>();
    this.bodyStmts = new StringBuilder();
    this.indentLevel = 1; // inside class { method { ... } }
  }

  /**
   * Exit the current method scope.
   * Returns the complete method body string (decls + temps + stmts),
   * each line indented with one tab (method-body level).
   * Clears the per-method buffers.
   */
  public String exitMethodScope() {
    StringBuilder sb = new StringBuilder();
    for (String d : varDecls)
      sb.append("    ").append(d).append("\n");
    for (String d : tempDecls)
      sb.append("    ").append(d).append("\n");
    sb.append(bodyStmts);

    // Reset
    varDecls = null;
    tempDecls = null;
    bodyStmts = null;
    // currentClass = null; <-- REMOVE THIS LINE
    currentMethod = null;
    indentLevel = 0;
    return sb.toString();
  }

  // -------------------------------------------------------
  // Emission helpers
  // -------------------------------------------------------

  /** Emit a variable declaration into the var-decls buffer. */
  public void emitVarDecl(String decl) {
    if (varDecls != null)
      varDecls.add(decl);
  }

  /**
   * Emit a statement line into the body-statements buffer,
   * indented according to the current indentLevel.
   */
  public void emitStmt(String stmt) {
    if (bodyStmts == null)
      return;
    bodyStmts.append(indent()).append(stmt).append("\n");
  }

  /** Append a line directly to the global output. */
  public void emitGlobal(String line) {
    globalOutput.append(line).append("\n");
  }

  /** Increase indent (e.g. entering an if-block body). */
  public void pushIndent() {
    indentLevel++;
  }

  /** Decrease indent (e.g. leaving an if-block body). */
  public void popIndent() {
    if (indentLevel > 0)
      indentLevel--;
  }

  // -------------------------------------------------------
  // Temporary variable management
  // -------------------------------------------------------

  /**
   * Allocate a fresh temp variable of the given type.
   * The declaration is added to tempDecls so it will appear at the top
   * of the method body in the final output.
   * Returns the temp's name.
   */
  public String newTemp(String type) {
    String name = "_t" + (tempCount++);
    if (tempDecls != null) {
      tempDecls.add(type + " " + name + ";");
    }
    return name;
  }

  // -------------------------------------------------------
  // Private helpers
  // -------------------------------------------------------

  private String indent() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < indentLevel; i++)
      sb.append("    ");
    return sb.toString();
  }
}
