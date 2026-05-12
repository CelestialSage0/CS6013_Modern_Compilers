package visitor;

import syntaxtree.*;
import java.util.*;

public class MethodInfo {
  public String name;
  public String returnType;
  public String ownerClass;

  // ordered list of parameter names
  public List<String> paramNames = new ArrayList<>();
  // param name -> declared type
  public Map<String, String> paramTypes = new LinkedHashMap<>();

  // ordered list of local variable names (excluding params)
  public List<String> localNames = new ArrayList<>();
  // local name -> declared type
  public Map<String, String> localTypes = new LinkedHashMap<>();

  // AST node for the full method declaration
  public MethodDeclaration astNode;

  public MethodInfo(String name, String returnType, String ownerClass) {
    this.name = name;
    this.returnType = returnType;
    this.ownerClass = ownerClass;
  }

  /** All variables declared in the method (params + locals), name -> type */
  public Map<String, String> allVarTypes() {
    Map<String, String> all = new LinkedHashMap<>();
    all.putAll(paramTypes);
    all.putAll(localTypes);
    return all;
  }
}
