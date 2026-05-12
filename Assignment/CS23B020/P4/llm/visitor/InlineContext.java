package visitor;

import syntaxtree.*;
import java.util.*;

public class InlineContext {
    public Map<String, ClassInfo> classTable;

    /**
     * * Represents a unique context-sensitive path to a call site.
     * E.g., [main_foo_call_node, internal_bar_call_node]
     */
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
            CallPath cp = (CallPath) o;
            return path.equals(cp.path); // Relies on AST Node reference equality
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }
    }

    // --- State for Analysis & Generation ---
    public Map<String, String> pointsTo = new LinkedHashMap<>();
    public Stack<Node> callStack = new Stack<>();

    // The Bridge: Maps a context-sensitive path to a resolved concrete class
    public Map<CallPath, String> callSiteResolutions = new HashMap<>();

    public Map<String, String> varSubst = new LinkedHashMap<>();
    public List<String> pendingVarDecls = new ArrayList<>();
    public StringBuilder sb = new StringBuilder();
    public String indent = "";
    public String currentClass = null;
    public int counter = 0;

    public InlineContext(Map<String, ClassInfo> classTable) {
        this.classTable = classTable;
    }

    public String freshTemp(String baseName) {
        return "il_" + (counter++) + "_" + baseName;
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