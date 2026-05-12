package visitor;

import syntaxtree.*;
import java.util.*;

public class InlineContext {
    public Map<String, ClassInfo> classTable;

    public static class CallPath {
        private final List<Node> path;

        public CallPath(List<Node> stack, Node current) {
            this.path = new ArrayList<>(stack);
            this.path.add(current);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            return path.equals(((CallPath) o).path);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }
    }

    public Map<String, String> pointsTo = new LinkedHashMap<>();
    public Stack<Node> callStack = new Stack<>();
    public Map<CallPath, String> callSiteResolutions = new HashMap<>();

    public Set<MethodInfo> activeMethods = new HashSet<>();
    public Set<MethodInfo> recursiveMethods = new HashSet<>();

    public Map<String, String> varSubst = new LinkedHashMap<>();
    public List<String> pendingVarDecls = new ArrayList<>();
    public StringBuilder sb = new StringBuilder();
    public String indent = "";
    public String currentClass = null;
    public int counter = 0;

    // ==============================================================================
    // NEW: Type Environment Tracking
    // ==============================================================================
    public MethodInfo currentMethod = null;
    public Map<String, String> tempTypes = new HashMap<>(); // Tracks types of generated il_ vars

    public InlineContext(Map<String, ClassInfo> classTable) {
        this.classTable = classTable;
    }

    public String freshTemp(String baseName, String type) {
        String tmp = "il_" + (counter++) + "_" + baseName;
        tempTypes.put(tmp, type); // Record the declared type of the fresh temp
        return tmp;
    }

    // Resolves the declared type of any variable in the current scope
    // Resolves the declared type of any variable in the current scope
    // Resolves the declared type of any variable in the current scope
    public String getDeclaredType(String varName) {
        if (varName.equals("this"))
            return currentClass;

        if (varName.startsWith("new ")) {
            int spaceIndex = varName.indexOf(' ', 4);
            if (spaceIndex != -1)
                return varName.substring(4, spaceIndex);
        }

        // ==============================================================================
        // CRITICAL FIX: Handle dynamically translated bare-fields (e.g., "b.first")
        // ==============================================================================
        if (varName.contains(".")) {
            String[] parts = varName.split("\\.");
            String baseType = getDeclaredType(parts[0]);
            if (baseType != null) {
                ClassInfo ci = classTable.get(baseType);
                while (ci != null) {
                    if (ci.fields.containsKey(parts[1]))
                        return ci.fields.get(parts[1]);
                    if (ci.parent == null)
                        break;
                    ci = classTable.get(ci.parent);
                }
            }
            return null;
        }

        if (tempTypes.containsKey(varName))
            return tempTypes.get(varName);

        if (currentMethod != null) {
            for (String[] loc : currentMethod.locals) {
                if (loc[1].equals(varName))
                    return loc[0];
            }
            for (String[] param : currentMethod.params) {
                if (param[1].equals(varName))
                    return param[0];
            }
        }

        ClassInfo ci = classTable.get(currentClass);
        while (ci != null) {
            if (ci.fields.containsKey(varName))
                return ci.fields.get(varName);
            if (ci.parent == null)
                break;
            ci = classTable.get(ci.parent);
        }
        return null;
    }

    // Checks if 'sub' is a subtype of 'sup' (or equal)
    public boolean isSubtype(String sub, String sup) {
        if (sub == null || sup == null)
            return false;
        if (sub.equals(sup))
            return true;
        ClassInfo ci = classTable.get(sub);
        while (ci != null && ci.parent != null) {
            if (ci.parent.equals(sup))
                return true;
            ci = classTable.get(ci.parent);
        }
        return false;
    }

    public void indent() {
        indent += "    ";
    }

    public void dedent() {
        indent = indent.length() >= 4 ? indent.substring(4) : "";
    }

    public void emit(String s) {
        sb.append(s);
    }

    public void emitLine(String s) {
        sb.append(indent).append(s).append("\n");
    }

    public void emitLine() {
        sb.append("\n");
    }

    public String subst(String name) {
        return varSubst.getOrDefault(name, name);
    }

    public Map<String, String> snapshotPointsTo() {
        return new LinkedHashMap<>(pointsTo);
    }
}