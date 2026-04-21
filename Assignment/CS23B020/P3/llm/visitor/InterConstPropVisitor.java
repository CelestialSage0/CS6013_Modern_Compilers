package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Interprocedural Constant Propagation (context-insensitive).
 *
 * KEY FIX: analyses methods in reverse-call order (callees before callers)
 * so that when main is analysed, live's return value is already in
 * methodStates. This ensures evalMessageSend folds correctly in a single
 * reAnalyse pass.
 *
 * Algorithm:
 * 1. Build call-site index (once).
 * 2. Compute topological order of methods (callees first).
 * 3. Loop:
 * a. Collect argument CPValues at every call site -> meet into param summaries.
 * b. Re-analyse all methods IN TOPOLOGICAL ORDER with updated summaries.
 * c. Repeat until no state changes.
 */
public class InterConstPropVisitor {

    private final ClassHierarchy ch;
    private final SymbolTable st;
    private final CallGraph cg;
    private final ConstPropVisitor cpv;

    // callerKey -> list of MessageSend nodes in that method
    private final Map<String, List<MessageSend>> callSiteIndex = new LinkedHashMap<>();

    // Topological order: callees before callers
    private List<String> topoOrder = new ArrayList<>();

    public InterConstPropVisitor(ClassHierarchy ch,
            SymbolTable st,
            CallGraph cg,
            ConstPropVisitor cpv) {
        this.ch = ch;
        this.st = st;
        this.cg = cg;
        this.cpv = cpv;
    }

    // ---------------------------------------------------------------
    // Main entry point
    // ---------------------------------------------------------------

    public void run(Goal goal) {
        buildCallSiteIndex(goal);
        buildTopoOrder();

        boolean changed = true;
        while (changed) {
            collectAndInjectArguments();
            changed = cpv.reAnalyseInOrder(topoOrder);
        }
    }

    // ---------------------------------------------------------------
    // Build call-site index: one list of MessageSend per reachable method
    // ---------------------------------------------------------------

    private void buildCallSiteIndex(Goal goal) {
        MessageSendCollector collector = new MessageSendCollector();
        MainClass mainNode = cpv.mainClassNode();

        for (String key : cg.reachableSet()) {
            String[] p = key.split("::");
            String cls = p[0], method = p[1];
            List<MessageSend> calls = new ArrayList<>();

            if ("main".equals(method) && mainNode != null) {
                mainNode.f15.accept(collector, calls);
            } else {
                MethodDeclaration md = lookupMD(cls, method);
                if (md != null)
                    md.f8.accept(collector, calls);
            }
            callSiteIndex.put(key, calls);
        }
    }

    // ---------------------------------------------------------------
    // Topological sort: callees appear before callers in the list.
    //
    // We reverse-DFS the call graph: start from leaves (methods that
    // call nothing), work up to callers. Cycles (mutual recursion) are
    // broken by the existing meet-based fixpoint — we just pick any order.
    // ---------------------------------------------------------------

    private void buildTopoOrder() {
        // Build adjacency: caller -> set of callees (reachable keys)
        Map<String, Set<String>> callees = new LinkedHashMap<>();
        for (String callerKey : cg.reachableSet()) {
            Set<String> targets = new LinkedHashSet<>();
            List<MessageSend> calls = callSiteIndex.getOrDefault(
                    callerKey, Collections.emptyList());
            String[] cp2 = callerKey.split("::");
            String callerCls = cp2[0], callerMeth = cp2[1];

            for (MessageSend ms : calls) {
                String methodName = ms.f2.f0.tokenImage;
                String receiverType = resolveReceiverType(ms, callerCls, callerMeth);
                if (receiverType == null)
                    continue;

                Set<String> candidates = new LinkedHashSet<>();
                candidates.add(receiverType);
                candidates.addAll(ch.allSubclasses(receiverType));
                for (String candidate : candidates) {
                    String dc = ch.declaringClass(candidate, methodName);
                    if (dc != null && cg.reachable(dc, methodName))
                        targets.add(dc + "::" + methodName);
                }
            }
            callees.put(callerKey, targets);
        }

        // Post-order DFS (callees visited before callers in result)
        Set<String> visited = new LinkedHashSet<>();
        List<String> result = new ArrayList<>();
        for (String key : cg.reachableSet())
            dfs(key, callees, visited, result);

        // result is post-order: callees come AFTER callers in DFS post-order.
        // We want callees FIRST, so reverse it.
        // Collections.reverse(result);DEBUG
        topoOrder = result;
    }

    private void dfs(String key, Map<String, Set<String>> callees,
            Set<String> visited, List<String> result) {
        if (!visited.add(key))
            return;
        for (String callee : callees.getOrDefault(key, Collections.emptySet()))
            dfs(callee, callees, visited, result);
        result.add(key);
    }

    // ---------------------------------------------------------------
    // Collect argument values from call sites -> inject into param summaries
    // ---------------------------------------------------------------

    private void collectAndInjectArguments() {
        // Fresh accumulators for this iteration
        Map<String, Map<String, CPValue>> newSummaries = new LinkedHashMap<>();

        for (Map.Entry<String, List<MessageSend>> entry : callSiteIndex.entrySet()) {
            String[] callerParts = entry.getKey().split("::");
            String callerCls = callerParts[0];
            String callerMeth = callerParts[1];

            for (MessageSend ms : entry.getValue()) {
                String calledMethod = ms.f2.f0.tokenImage;
                String receiverType = resolveReceiverType(ms, callerCls, callerMeth);
                if (receiverType == null)
                    continue;

                List<CPValue> argVals = evaluateArgs(ms, callerCls, callerMeth);

                Set<String> candidates = new LinkedHashSet<>();
                candidates.add(receiverType);
                candidates.addAll(ch.allSubclasses(receiverType));

                for (String candidate : candidates) {
                    String declaringCls = ch.declaringClass(candidate, calledMethod);
                    if (declaringCls == null || !cg.reachable(declaringCls, calledMethod))
                        continue;

                    List<String> params = st.paramsOf(declaringCls, calledMethod);
                    String summaryKey = declaringCls + "::" + calledMethod;
                    Map<String, CPValue> summary = newSummaries.computeIfAbsent(
                            summaryKey, k -> new LinkedHashMap<>());

                    for (int i = 0; i < Math.min(params.size(), argVals.size()); i++) {
                        String param = params.get(i);
                        CPValue argVal = argVals.get(i);
                        CPValue existing = summary.getOrDefault(param, CPValue.UNDEF);
                        summary.put(param, CPValue.meet(existing, argVal));
                    }
                }
            }
        }

        // Inject: methods with no observed call sites get NAC for all params
        for (String key : cg.reachableSet()) {
            String[] p = key.split("::");
            String cls = p[0], method = p[1];
            List<String> params = st.paramsOf(cls, method);
            if (params.isEmpty())
                continue;

            Map<String, CPValue> summary = newSummaries.get(key);
            for (String param : params) {
                CPValue val = (summary == null || !summary.containsKey(param))
                        ? CPValue.NAC
                        : summary.get(param);
                if (val.isUndef())
                    val = CPValue.NAC;
                cpv.setParamSummary(cls, method, param, val);
            }
        }
    }

    // ---------------------------------------------------------------
    // Evaluate argument identifiers from caller's current CP state
    // ---------------------------------------------------------------

    private List<CPValue> evaluateArgs(MessageSend ms,
            String callerCls, String callerMeth) {
        List<CPValue> vals = new ArrayList<>();
        if (!ms.f4.present())
            return vals;
        ArgList al = (ArgList) ms.f4.node;
        vals.add(cpv.lookupVarInMethod(callerCls, callerMeth, al.f0.f0.tokenImage));
        if (al.f1.present())
            for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();) {
                ArgRest ar = (ArgRest) e.nextElement();
                vals.add(cpv.lookupVarInMethod(callerCls, callerMeth, ar.f1.f0.tokenImage));
            }
        return vals;
    }

    // ---------------------------------------------------------------
    // Receiver type resolution
    // ---------------------------------------------------------------

    private String resolveReceiverType(MessageSend ms, String cls, String method) {
        Node choice = ms.f0.f0.choice;
        if (choice instanceof Identifier)
            return st.typeOf(cls, method, ((Identifier) choice).f0.tokenImage);
        if (choice instanceof ThisExpression)
            return cls;
        if (choice instanceof AllocationExpression)
            return ((AllocationExpression) choice).f1.f0.tokenImage;
        return null;
    }

    private MethodDeclaration lookupMD(String cls, String method) {
        String cur = cls;
        while (cur != null) {
            Map<String, MethodDeclaration> m = cpv.methodIndex().get(cur);
            if (m != null && m.containsKey(method))
                return m.get(method);
            cur = ch.parentOf(cur);
        }
        return null;
    }

    private static class MessageSendCollector extends GJDepthFirst {
        @SuppressWarnings("unchecked")
        @Override
        public Object visit(MessageSend n, Object argu) {
            ((List<MessageSend>) argu).add(n);
            n.f4.accept(this, argu);
            return null;
        }
    }
}