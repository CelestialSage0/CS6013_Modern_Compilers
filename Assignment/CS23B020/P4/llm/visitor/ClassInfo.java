package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Holds all information about a class extracted during the first pass:
 * - parent class name
 * - field declarations (type -> name)
 * - method declarations (method name -> MethodInfo)
 */
public class ClassInfo {
    public String name;
    public String parent; // null if no extends
    public Map<String, String> fields = new LinkedHashMap<>(); // varName -> typeName
    public Map<String, MethodInfo> methods = new LinkedHashMap<>();

    public ClassInfo(String name, String parent) {
        this.name = name;
        this.parent = parent;
    }

    /** Returns the MethodInfo for methodName, searching up the hierarchy. */
    public MethodInfo lookupMethod(String methodName, Map<String, ClassInfo> classTable) {
        if (methods.containsKey(methodName))
            return methods.get(methodName);
        if (parent != null && classTable.containsKey(parent))
            return classTable.get(parent).lookupMethod(methodName, classTable);
        return null;
    }

    /**
     * Returns true if this class or any subclass (anywhere in classTable)
     * overrides methodName with its own definition. Used to detect polymorphism.
     */
    public boolean hasSubclassOverride(String methodName, Map<String, ClassInfo> classTable) {
        for (ClassInfo ci : classTable.values()) {
            if (ci == this)
                continue;
            if (isAncestorOf(ci, classTable)) {
                // ci is a subclass of this; does it define methodName directly?
                if (ci.methods.containsKey(methodName))
                    return true;
            }
        }
        return false;
    }

    /** True if other is a (direct or indirect) subclass of this. */
    public boolean isAncestorOf(ClassInfo other, Map<String, ClassInfo> classTable) {
        String p = other.parent;
        while (p != null) {
            if (p.equals(this.name))
                return true;
            ClassInfo pc = classTable.get(p);
            if (pc == null)
                break;
            p = pc.parent;
        }
        return false;
    }
}