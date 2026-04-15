import java.util.*;

/**
 * Symbol Table: stores class/method/field/variable info collected in Pass 1.
 * Used by CodeGenVisitor in Pass 2 to infer expression types for temp
 * variables.
 */
public class SymbolTable {

  // Top-level map: class name -> ClassInfo
  private final Map<String, ClassInfo> classes = new LinkedHashMap<>();

  // -------------------------------------------------------
  // API used by SymbolTableVisitor (Pass 1)
  // -------------------------------------------------------

  public void addClass(String name, String superClass) {
    if (!classes.containsKey(name)) {
      classes.put(name, new ClassInfo(name, superClass));
    }
  }

  public ClassInfo getClass(String name) {
    return classes.get(name);
  }

  public boolean hasClass(String name) {
    return classes.containsKey(name);
  }

  public Map<String, ClassInfo> getAllClasses() {
    return classes;
  }

  // -------------------------------------------------------
  // API used by CodeGenVisitor (Pass 2) — type lookup
  // -------------------------------------------------------

  /**
   * Look up the type of a variable in the given method, then class fields,
   * then superclass fields (walking the inheritance chain).
   */
  public String lookupVarType(String varName, String className, String methodName) {
    if (className == null)
      return "Object";
    ClassInfo ci = classes.get(className);
    if (ci == null)
      return "Object";

    // 1. Check method locals/params
    if (methodName != null) {
      MethodInfo mi = ci.getMethod(methodName);
      if (mi != null) {
        String t = mi.lookupVar(varName);
        if (t != null)
          return t;
      }
    }

    // 2. Check class fields
    String fieldType = ci.getFieldType(varName);
    if (fieldType != null)
      return fieldType;

    // 3. Walk superclass chain
    if (ci.superClass != null) {
      return lookupVarType(varName, ci.superClass, null);
    }

    return "Object"; // fallback
  }

  /**
   * Get the return type of a method, walking the inheritance chain.
   */
  public String getMethodReturnType(String className, String methodName) {
    if (className == null)
      return "Object";
    ClassInfo ci = classes.get(className);
    if (ci == null)
      return "Object";
    MethodInfo mi = ci.getMethod(methodName);
    if (mi != null)
      return mi.returnType;
    if (ci.superClass != null)
      return getMethodReturnType(ci.superClass, methodName);
    return "Object";
  }

  // -------------------------------------------------------
  // Inner classes
  // -------------------------------------------------------

  public static class ClassInfo {
    public final String name;
    public final String superClass; // null if no "extends"

    private final Map<String, String> fields = new LinkedHashMap<>();
    private final Map<String, MethodInfo> methods = new LinkedHashMap<>();

    public ClassInfo(String name, String superClass) {
      this.name = name;
      this.superClass = superClass;
    }

    public void addField(String name, String type) {
      fields.put(name, type);
    }

    public String getFieldType(String name) {
      return fields.get(name);
    }

    public void addMethod(MethodInfo mi) {
      methods.put(mi.name, mi);
    }

    public MethodInfo getMethod(String name) {
      return methods.get(name);
    }

    public Map<String, String> getFields() {
      return fields;
    }

    public Map<String, MethodInfo> getMethods() {
      return methods;
    }
  }

  public static class MethodInfo {
    public final String name;
    public final String returnType;

    // Ordered list of parameters as [type, name] pairs
    private final List<String[]> params = new ArrayList<>();
    // All locals (params + declared vars): name -> type
    private final Map<String, String> locals = new LinkedHashMap<>();

    public MethodInfo(String name, String returnType) {
      this.name = name;
      this.returnType = returnType;
    }

    public void addParam(String type, String name) {
      params.add(new String[] { type, name });
      locals.put(name, type);
    }

    public void addLocal(String name, String type) {
      locals.put(name, type);
    }

    /** Returns type of local/param, or null if not found. */
    public String lookupVar(String name) {
      return locals.get(name);
    }

    public List<String[]> getParams() {
      return params;
    }

    public Map<String, String> getLocals() {
      return locals;
    }
  }
}
