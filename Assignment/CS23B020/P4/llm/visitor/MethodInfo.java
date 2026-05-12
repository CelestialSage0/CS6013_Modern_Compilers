package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * All information about a single method needed for inlining.
 */
public class MethodInfo {
    public String ownerClass;
    public String name;
    public String returnTypeName; // "int", "boolean", "int[]", or class name

    // Parameters in order: each entry is [typeName, paramName]
    public List<String[]> params = new ArrayList<>();

    // Local variable declarations in order: each entry is [typeName, varName]
    public List<String[]> locals = new ArrayList<>();

    // The AST node of the method – kept so we can inline by walking it
    public MethodDeclaration astNode;

    public MethodInfo(String ownerClass, String name, String returnTypeName,
            MethodDeclaration astNode) {
        this.ownerClass = ownerClass;
        this.name = name;
        this.returnTypeName = returnTypeName;
        this.astNode = astNode;
    }
}