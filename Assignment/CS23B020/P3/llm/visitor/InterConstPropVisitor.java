package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Interprocedural Constant Propagation (context-insensitive).
 *
 * Algorithm:
 * For each parameter of each reachable method, compute the MEET of
 * the CP values of all arguments passed at every call site that targets
 * that method (via CHA). Inject the result into ConstPropVisitor as the
 * parameter's initial value (instead of NAC). Re-run CP. Repeat until
 * no method state changes.
 *
 * Context-insensitivity: one summary per (class, method, param) — we do
 * NOT distinguish different call sites. If foo is called with 2 at one
 * site and 3 at another, param gets NAC.
 *
 * CHA conservatism: if a method is a CHA target (reachable via any
 * subclass override), ALL overriding methods are targets too. We meet
 * across all of them — an override whose param is NAC makes the shared
 * summary NAC for that position.
 *
 * Argument value source: after each CP run, we read argument values from
 * the CALLER's final CP state. A literal argument (new Foo(), true, 3...)
 * is evaluated directly.
 *
 * Usage: call run(goal) once, then use cpv for subsequent passes.
 */
public class InterConstPropVisitor {

    private final ClassHierarchy ch;
    private final SymbolTable st;
    private final CallGraph cg;
    private final ConstPropVisitor cpv;

    // ---------------------------------------------------------------
    // Call-site index: for each (callee class, callee method, param index)
    // we accumulate the meet of all argument CPValues seen across all
    // call sites (in all reachable callers).
    // ---------------------------------------------------------------

    // "callerCls::callerMethod" -> list of MessageSend nodes in that method
    // (built once from the reachable set)
    private final Map<String, List<MessageSend>> callSiteIndex = new LinkedHashMap<>();

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------
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

    /**
     * Run interprocedural CP to fixpoint.
     * After this returns, cpv holds the fully refined per-method states.
     */
    public void run(Goal goal) {
        // Step 1: build the call-site index (done once — AST doesn't change)
        buildCallSiteIndex(goal);

        // Step 2: iterate until no CP state changes
        boolean changed = true;
        while (changed) {
            // Collect argument values at every call site using current CP state
            collectAndInjectArguments();
            // Re-run intraprocedural CP with updated param summaries
            changed = cpv.reAnalyse();
        }
    }

    // ---------------------------------------------------------------
    // Step 1: collect all MessageSend nodes per reachable caller method
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
                Map<String, MethodDeclaration> mmap = cpv.methodIndex().get(cls);
                if (mmap != null) {
                    MethodDeclaration md = mmap.get(method);
                    // Walk up hierarchy if not found directly
                    if (md == null) {
                        String cur = ch.parentOf(cls);
                        while (cur != null && md == null) {
                            Map<String, MethodDeclaration> pm = cpv.methodIndex().get(cur);
                            if (pm != null)
                                md = pm.get(method);
                            cur = ch.parentOf(cur);
                        }
                    }
                    if (md != null)
                        md.f8.accept(collector, calls);
                }
            }
            callSiteIndex.put(key, calls);
        }
    }

    // ---------------------------------------------------------------
    // Step 2: for each call site, resolve targets via CHA, evaluate
    // arguments from caller's CP state, meet into param summaries
    // ---------------------------------------------------------------

    private void collectAndInjectArguments() {
        // Accumulator: "calleeCls::calleeMethod" -> paramName -> meet-of-args
        // We rebuild from scratch each iteration (CP state has changed)
        Map<String, Map<String, CPValue>> newSummaries = new LinkedHashMap<>();

        for (Map.Entry<String, List<MessageSend>> entry : callSiteIndex.entrySet()) {
            String[] callerParts = entry.getKey().split("::");
            String callerCls = callerParts[0];
            String callerMeth = callerParts[1];

            for (MessageSend ms : entry.getValue()) {
                String calledMethod = ms.f2.f0.tokenImage;

                // Resolve receiver type for CHA
                String receiverType = resolveReceiverType(ms, callerCls, callerMeth);
                if (receiverType == null)
                    continue;

                // CHA: all candidate targets
                Set<String> candidates = new LinkedHashSet<>();
                candidates.add(receiverType);
                candidates.addAll(ch.allSubclasses(receiverType));

                // Evaluate argument CPValues from caller's current CP state
                List<CPValue> argVals = evaluateArgs(ms, callerCls, callerMeth);

                // For each CHA target, meet argVals into that target's summary
                for (String candidate : candidates) {
                    String declaringCls = ch.declaringClass(candidate, calledMethod);
                    if (declaringCls == null)
                        continue;
                    if (!cg.reachable(declaringCls, calledMethod))
                        continue;

                    List<String> params = st.paramsOf(declaringCls, calledMethod);
                    String summaryKey = declaringCls + "::" + calledMethod;

                    Map<String, CPValue> summary = newSummaries.computeIfAbsent(
                            summaryKey, k -> new LinkedHashMap<>());

                    for (int i = 0; i < Math.min(params.size(), argVals.size()); i++) {
                        String paramName = params.get(i);
                        CPValue argVal = argVals.get(i);
                        // Meet: if this param was seen before, meet with new value
                        CPValue existing = summary.getOrDefault(paramName, CPValue.UNDEF);
                        summary.put(paramName, CPValue.meet(existing, argVal));
                    }
                }
            }
        }

        // Any reachable method that has NO call sites targeting it gets NAC
        // for all params (conservative — could be called from outside).
        // We detect this by checking if a key appears in newSummaries.
        // If a (cls, method) has parameters but no summary entry -> NAC for all.

        // Inject final summaries into cpv
        for (String key : cg.reachableSet()) {
            String[] p = key.split("::");
            String cls = p[0], method = p[1];
            List<String> params = st.paramsOf(cls, method);
            if (params.isEmpty())
                continue;

            Map<String, CPValue> summary = newSummaries.get(key);

            for (String param : params) {
                CPValue val;
                if (summary == null || !summary.containsKey(param)) {
                    // No call site provided this argument -> NAC (unknown callers)
                    val = CPValue.NAC;
                } else {
                    val = summary.get(param);
                    // UNDEF means no call site was seen at all -> NAC (conservative)
                    if (val.isUndef())
                        val = CPValue.NAC;
                }
                cpv.setParamSummary(cls, method, param, val);
            }
        }
    }

    // ---------------------------------------------------------------
    // Evaluate the argument list of a MessageSend using the caller's
    // current CP state.
    //
    // In TACoJava, ArgList is: Identifier ( "," Identifier )*
    // Arguments are always identifiers (not nested expressions).
    // We look up each identifier's CPValue in the caller's state.
    // ---------------------------------------------------------------

    private List<CPValue> evaluateArgs(MessageSend ms,
            String callerCls,
            String callerMeth) {
        List<CPValue> vals = new ArrayList<>();
        if (!ms.f4.present())
            return vals;

        ArgList al = (ArgList) ms.f4.node;
        // First argument
        vals.add(lookupArgValue(al.f0.f0.tokenImage, callerCls, callerMeth));
        // Remaining arguments
        if (al.f1.present()) {
            for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();) {
                ArgRest ar = (ArgRest) e.nextElement();
                vals.add(lookupArgValue(ar.f1.f0.tokenImage, callerCls, callerMeth));
            }
        }
        return vals;
    }

    /**
     * Look up the CPValue of an argument identifier in the caller's state.
     * Uses cpv.lookupVarInMethod which consults the caller's final CP state.
     */
    private CPValue lookupArgValue(String varName,
            String callerCls,
            String callerMeth) {
        return cpv.lookupVarInMethod(callerCls, callerMeth, varName);
    }

    // ---------------------------------------------------------------
    // Resolve declared type of MessageSend receiver (same as CallGraphVisitor)
    // ---------------------------------------------------------------

    private String resolveReceiverType(MessageSend ms,
            String enclosingCls,
            String enclosingMeth) {
        Node choice = ms.f0.f0.choice;
        if (choice instanceof Identifier) {
            return st.typeOf(enclosingCls, enclosingMeth,
                    ((Identifier) choice).f0.tokenImage);
        }
        if (choice instanceof ThisExpression)
            return enclosingCls;
        if (choice instanceof AllocationExpression)
            return ((AllocationExpression) choice).f1.f0.tokenImage;
        return null;
    }

    // ---------------------------------------------------------------
    // Inner helper: collects MessageSend nodes from a subtree
    // ---------------------------------------------------------------
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