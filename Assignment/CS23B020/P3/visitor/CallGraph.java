package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 3: Call Graph construction via Class Hierarchy Analysis (CHA).
 *
 * CHA resolution rule for a call x.foo(args) where x has declared type T:
 * - Include T.foo if T (or an ancestor) declares foo
 * - Include C.foo for every strict subclass C of T (transitively) that
 * inherits or declares foo
 *
 * Algorithm (worklist):
 * 1. Seed worklist with (mainClassName, "main")
 * 2. For each reachable (class, method), collect all MessageSend nodes
 * in its body
 * 3. CHA-resolve each call site -> add newly found callees to worklist
 * 4. Repeat until fixpoint
 *
 * After visiting the Goal node, use reachable(className, methodName)
 * to test liveness.
 */
public class CallGraph extends GJDepthFirst {

  private final ClassHierarchy ch;
  private final SymbolTable st;
  private final String mainClassName;

  // ---------------------------------------------------------------
  // Phase 1 index: className -> methodName -> MethodDeclaration node
  // ---------------------------------------------------------------
  private final Map<String, Map<String, MethodDeclaration>> methodIndex = new LinkedHashMap<>();

  // Captured during indexing for scanning main's statements
  private MainClass mainClassNode = null;

  // ---------------------------------------------------------------
  // Reachable set (stored as "ClassName::methodName" strings)
  // ---------------------------------------------------------------
  private final Set<String> reachableKeys = new LinkedHashSet<>();

  // ---------------------------------------------------------------
  // Constructor
  // ---------------------------------------------------------------
  public CallGraph(ClassHierarchy ch,
      SymbolTable st,
      String mainClassName) {
    this.ch = ch;
    this.st = st;
    this.mainClassName = mainClassName;
  }

  // ---------------------------------------------------------------
  // Public query API
  // ---------------------------------------------------------------

  /** Is (className, methodName) reachable from main? */
  public boolean reachable(String className, String methodName) {
    return reachableKeys.contains(key(className, methodName));
  }

  /** Full reachable set as unmodifiable "ClassName::methodName" strings. */
  public Set<String> reachableSet() {
    return Collections.unmodifiableSet(reachableKeys);
  }

  // ---------------------------------------------------------------
  // Two-phase entry point via Goal
  // ---------------------------------------------------------------

  /**
   * f0 -> MainClass()
   * f1 -> ( TypeDeclaration() )*
   * f2 -> <EOF>
   */
  @Override
  public Object visit(Goal n, Object argu) {
    // Phase 1: index all method AST nodes & capture mainClassNode
    n.f0.accept(this, "INDEX");
    n.f1.accept(this, "INDEX");

    // Phase 2: worklist propagation
    propagate();
    return null;
  }

  // ---------------------------------------------------------------
  // Phase 1 visitors — argu == "INDEX"
  // ---------------------------------------------------------------

  /** Capture the MainClass node so we can scan its statements later. */
  @Override
  public Object visit(MainClass n, Object argu) {
    mainClassNode = n;
    return null;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> ( VarDeclaration() )*
   * f4 -> ( MethodDeclaration() )*
   * f5 -> "}"
   */
  @Override
  public Object visit(ClassDeclaration n, Object argu) {
    if (!"INDEX".equals(argu))
      return null;
    String className = n.f1.f0.tokenImage;
    n.f4.accept(this, className);
    return null;
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "extends"
   * f3 -> Identifier()
   * f4 -> "{"
   * f5 -> ( VarDeclaration() )*
   * f6 -> ( MethodDeclaration() )*
   * f7 -> "}"
   */
  @Override
  public Object visit(ClassExtendsDeclaration n, Object argu) {
    if (!"INDEX".equals(argu))
      return null;
    String className = n.f1.f0.tokenImage;
    n.f6.accept(this, className);
    return null;
  }

  /**
   * Index this node under (className, methodName).
   * argu = enclosing class name (String) during indexing.
   *
   * f0 -> "public"
   * f1 -> Type()
   * f2 -> Identifier() <- method name
   * f3 -> "("
   * f4 -> ( FormalParameterList() )?
   * f5 -> ")"
   * f6 -> "{"
   * f7 -> ( VarDeclaration() )*
   * f8 -> ( Statement() )*
   * f9 -> "return"
   * f10 -> Identifier()
   * f11 -> ";"
   * f12 -> "}"
   */
  @Override
  public Object visit(MethodDeclaration n, Object argu) {
    if (!(argu instanceof String))
      return null;
    String className = (String) argu;
    String methodName = n.f2.f0.tokenImage;
    methodIndex.computeIfAbsent(className, k -> new LinkedHashMap<>())
        .put(methodName, n);
    return null;
  }

  // ---------------------------------------------------------------
  // Phase 2: worklist propagation
  // ---------------------------------------------------------------

  private void propagate() {
    Deque<String[]> worklist = new ArrayDeque<>();

    // Seed with the main method
    worklist.add(new String[] { mainClassName, "main" });

    while (!worklist.isEmpty()) {
      String[] entry = worklist.poll();
      String cls = entry[0];
      String method = entry[1];

      if (!reachableKeys.add(key(cls, method)))
        continue; // already seen

      // Collect all MessageSend nodes in this method's body
      List<MessageSend> calls = collectCalls(cls, method);

      // CHA-resolve each call and enqueue newly discovered callees
      for (MessageSend ms : calls) {
        String calledMethod = ms.f2.f0.tokenImage;
        String receiverType = resolveReceiverType(ms, cls, method);
        if (receiverType == null)
          continue; // unresolvable — skip

        // CHA: declared type + all transitive subclasses
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(receiverType);
        candidates.addAll(ch.allSubclasses(receiverType));

        for (String candidate : candidates) {
          String declaringCls = ch.declaringClass(candidate, calledMethod);
          if (declaringCls != null) {
            String k = key(declaringCls, calledMethod);
            if (!reachableKeys.contains(k)) {
              worklist.add(new String[] { declaringCls, calledMethod });
            }
          }
        }
      }
    }
  }

  // ---------------------------------------------------------------
  // Collect MessageSend nodes from a method's body
  // ---------------------------------------------------------------

  private List<MessageSend> collectCalls(String cls, String method) {
    List<MessageSend> out = new ArrayList<>();
    MessageSendCollector collector = new MessageSendCollector();

    if ("main".equals(method) && mainClassNode != null) {
      // f15 = ( Statement() )* in MainClass
      mainClassNode.f15.accept(collector, out);
    } else {
      MethodDeclaration md = lookupMethod(cls, method);
      if (md != null) {
        // f8 = ( Statement() )* in MethodDeclaration
        md.f8.accept(collector, out);
      }
    }
    return out;
  }

  // ---------------------------------------------------------------
  // CHA receiver type resolution
  // ---------------------------------------------------------------

  /**
   * Determine the DECLARED static type of the receiver in a MessageSend.
   *
   * TACoJava MessageSend:
   * PrimaryExpression "." Identifier "(" ArgList? ")"
   *
   * PrimaryExpression choices (f0.choice):
   * IntegerLiteral | TrueLiteral | FalseLiteral | Identifier
   * | ThisExpression | ArrayAllocationExpression
   * | AllocationExpression | NotExpression
   *
   * Resolvable:
   * Identifier -> symbol table lookup
   * ThisExpression -> enclosing class
   * AllocationExpression -> the allocated class name
   *
   * Everything else -> null (conservatively skip).
   */
  private String resolveReceiverType(MessageSend ms,
      String enclosingClass,
      String enclosingMethod) {
    // ms.f0 is PrimaryExpression; .f0 is NodeChoice
    Node choice = ms.f0.f0.choice;

    if (choice instanceof Identifier) {
      String varName = ((Identifier) choice).f0.tokenImage;
      return st.typeOf(enclosingClass, enclosingMethod, varName);
    }
    if (choice instanceof ThisExpression) {
      return enclosingClass;
    }
    if (choice instanceof AllocationExpression) {
      // new Foo() -- type is Foo
      return ((AllocationExpression) choice).f1.f0.tokenImage;
    }
    return null;
  }

  // ---------------------------------------------------------------
  // Utility
  // ---------------------------------------------------------------

  /**
   * Find the MethodDeclaration for (className, methodName), walking up
   * the inheritance chain if needed.
   */
  private MethodDeclaration lookupMethod(String className, String methodName) {
    String cur = className;
    while (cur != null) {
      Map<String, MethodDeclaration> m = methodIndex.get(cur);
      if (m != null && m.containsKey(methodName))
        return m.get(methodName);
      cur = ch.parentOf(cur);
    }
    return null;
  }

  private static String key(String cls, String method) {
    return cls + "::" + method;
  }

  // ---------------------------------------------------------------
  // Inner helper: depth-first collector of MessageSend nodes
  // ---------------------------------------------------------------
  private static class MessageSendCollector extends GJDepthFirst {

    @SuppressWarnings("unchecked")
    @Override
    public Object visit(MessageSend n, Object argu) {
      ((List<MessageSend>) argu).add(n);
      // Visit args too (TACoJava args are plain Identifiers,
      // but visit for completeness / future-proofing)
      n.f4.accept(this, argu);
      return null;
    }
  }
}
