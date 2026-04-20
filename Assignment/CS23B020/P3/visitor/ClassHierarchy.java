package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 1: Builds the class hierarchy.
 *
 * After visiting the AST, provides:
 * parentOf(C) -> direct parent class name, or null if none
 * childrenOf(C) -> direct subclass names
 * allSubclasses(C) -> transitive subclass names (computed on demand)
 * methodsOf(C) -> method names declared directly in C
 * allMethodsOf(C) -> method names declared in C or any ancestor
 * classNames() -> all class names (including main class)
 *
 * Usage:
 * ClassHierarchyVisitor ch = new ClassHierarchyVisitor();
 * goal.accept(ch, null);
 */
public class ClassHierarchy extends GJDepthFirst {

    // ---------------------------------------------------------------
    // Internal tables (populated during visiting)
    // ---------------------------------------------------------------

    /** Maps class name -> direct parent name (null if no extends) */
    private final Map<String, String> parentOf = new LinkedHashMap<>();

    /** Maps class name -> set of direct child class names */
    private final Map<String, Set<String>> childrenOf = new LinkedHashMap<>();

    /** Maps class name -> set of method names declared IN that class */
    private final Map<String, Set<String>> methodsOf = new LinkedHashMap<>();

    // ---------------------------------------------------------------
    // Public query API
    // ---------------------------------------------------------------

    /** All known class names (including the main class). */
    public Set<String> classNames() {
        return Collections.unmodifiableSet(parentOf.keySet());
    }

    /** Direct parent of C, or null. */
    public String parentOf(String className) {
        return parentOf.get(className);
    }

    /** Direct children of C (may be empty). */
    public Set<String> childrenOf(String className) {
        return childrenOf.getOrDefault(className, Collections.emptySet());
    }

    /** Methods declared directly in C (may be empty). */
    public Set<String> methodsOf(String className) {
        return methodsOf.getOrDefault(className, Collections.emptySet());
    }

    /**
     * All transitive subclasses of C (not including C itself).
     * Computed by BFS over childrenOf.
     */
    public Set<String> allSubclasses(String className) {
        Set<String> result = new LinkedHashSet<>();
        Queue<String> worklist = new LinkedList<>();
        worklist.addAll(childrenOf(className));
        while (!worklist.isEmpty()) {
            String c = worklist.poll();
            if (result.add(c)) { // if newly added
                worklist.addAll(childrenOf(c));
            }
        }
        return result;
    }

    /**
     * All method names visible in C: own methods UNION inherited methods
     * (walking up the parent chain).
     */
    public Set<String> allMethodsOf(String className) {
        Set<String> result = new LinkedHashSet<>();
        String cur = className;
        while (cur != null) {
            result.addAll(methodsOf(cur));
            cur = parentOf(cur);
        }
        return result;
    }

    /**
     * Returns true if className declares (directly) a method named methodName.
     */
    public boolean declaresMethod(String className, String methodName) {
        return methodsOf(className).contains(methodName);
    }

    /**
     * Finds the class in C's hierarchy (C itself or an ancestor) that
     * actually declares methodName. Returns null if not found.
     */
    public String declaringClass(String className, String methodName) {
        String cur = className;
        while (cur != null) {
            if (declaresMethod(cur, methodName))
                return cur;
            cur = parentOf(cur);
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Visitor methods
    // ---------------------------------------------------------------

    /**
     * f0 -> "class"
     * f1 -> Identifier() <- class name
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    @Override
    public Object visit(ClassDeclaration n, Object argu) {
        String name = n.f1.f0.tokenImage;
        registerClass(name, null);
        // Visit method declarations, passing the class name down
        n.f4.accept(this, name);
        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier() <- class name
     * f2 -> "extends"
     * f3 -> Identifier() <- parent name
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    @Override
    public Object visit(ClassExtendsDeclaration n, Object argu) {
        String name = n.f1.f0.tokenImage;
        String parent = n.f3.f0.tokenImage;
        registerClass(name, parent);
        // Record child link
        childrenOf.computeIfAbsent(parent, k -> new LinkedHashSet<>()).add(name);
        // Visit method declarations
        n.f6.accept(this, name);
        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier() <- main class name
     * ...
     * We register the main class so it shows up in classNames().
     */
    @Override
    public Object visit(MainClass n, Object argu) {
        String name = n.f1.f0.tokenImage;
        registerClass(name, null);
        // main class has no ordinary methods, so nothing more to do
        return null;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier() <- method name
     * f3 -> "("
     * ...
     * argu = enclosing class name (String)
     */
    @Override
    public Object visit(MethodDeclaration n, Object argu) {
        String className = (String) argu;
        String methodName = n.f2.f0.tokenImage;
        methodsOf.computeIfAbsent(className, k -> new LinkedHashSet<>()).add(methodName);
        return null;
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private void registerClass(String name, String parent) {
        parentOf.put(name, parent); // parent may be null
        // Ensure entries exist even if no children / methods are added later
        childrenOf.computeIfAbsent(name, k -> new LinkedHashSet<>());
        methodsOf.computeIfAbsent(name, k -> new LinkedHashSet<>());
    }
}
