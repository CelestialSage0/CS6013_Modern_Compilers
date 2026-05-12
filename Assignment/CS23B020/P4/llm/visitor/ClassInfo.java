package visitor;

import java.util.*;

public class ClassInfo {
  public String name;
  public String parent; // null if no extends

  // field name -> declared type string
  public Map<String, String> fields = new LinkedHashMap<>();

  // method name -> MethodInfo
  public Map<String, MethodInfo> methods = new LinkedHashMap<>();

  public ClassInfo(String name, String parent) {
    this.name = name;
    this.parent = parent;
  }
}
