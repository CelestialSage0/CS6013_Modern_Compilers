package visitor;

import syntaxtree.*;
import java.util.*;

/**
 * Pass 2: Context-sensitive flow-sensitive points-to analysis.
 */
public class PointsToVisitor extends GJDepthFirst<String, InlineContext> {

    @Override
    public String visit(Goal n, InlineContext ctx) {
        n.f0.accept(this, ctx);
        n.f1.accept(this, ctx);
        return null;
    }

    @Override
    public String visit(MainClass n, InlineContext ctx) {
        ctx.pointsTo.clear();
        ctx.callStack.clear();
        n.f15.accept(this, ctx); // visit statements
        return null;
    }

    @Override
    public String visit(ClassDeclaration n, InlineContext ctx) {
        n.f4.accept(this, ctx);
        return null;
    }

    @Override
    public String visit(ClassExtendsDeclaration n, InlineContext ctx) {
        n.f6.accept(this, ctx);
        return null;
    }

    @Override
    public String visit(MethodDeclaration n, InlineContext ctx) {
        // Only clear pointsTo if we are analyzing the method from the root (not
        // simulating an inline)
        if (ctx.callStack.isEmpty()) {
            ctx.pointsTo.clear();
            n.f8.accept(this, ctx);
        }
        return null;
    }

    @Override
    public String visit(AssignmentStatement n, InlineContext ctx) {
        String lhs = n.f0.f0.tokenImage;
        String rhs = n.f2.accept(this, ctx);

        if (rhs != null && rhs.startsWith("new ") && !rhs.startsWith("new int")) {
            String allocClass = rhs.substring(4, rhs.indexOf(' ', 4));
            ctx.pointsTo.put(lhs, allocClass);
        } else if (rhs != null && ctx.pointsTo.containsKey(rhs)) {
            // Track aliases (e.g. x = y;)
            ctx.pointsTo.put(lhs, ctx.pointsTo.get(rhs));
        } else {
            ctx.pointsTo.remove(lhs);
        }
        return null;
    }

    @Override
    public String visit(IfStatement n, InlineContext ctx) {
        Map<String, String> beforeBranch = ctx.snapshotPointsTo();
        n.f4.accept(this, ctx);
        Map<String, String> afterThen = ctx.snapshotPointsTo();

        ctx.pointsTo = new LinkedHashMap<>(beforeBranch);
        n.f6.accept(this, ctx);
        Map<String, String> afterElse = ctx.snapshotPointsTo();

        // Flow-sensitive merge
        Map<String, String> merged = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : afterThen.entrySet()) {
            String key = e.getKey();
            if (e.getValue().equals(afterElse.get(key))) {
                merged.put(key, e.getValue());
            }
        }
        ctx.pointsTo = merged;
        return null;
    }

    @Override
    public String visit(MessageSendStatement n, InlineContext ctx) {
        boolean doInline = n.f0.present();
        Node sendNode = n.f1.choice;
        MessageSend ms;
        String lhsVar = null;

        if (sendNode instanceof RetMessageSendStmt) {
            RetMessageSendStmt r = (RetMessageSendStmt) sendNode;
            ms = r.f2;
            lhsVar = r.f0.f0.tokenImage;
        } else {
            ms = ((VoidMessageSendStmt) sendNode).f0;
        }

        String receiver = ms.f0.accept(this, ctx);
        String concreteClass = ctx.pointsTo.get(receiver);

        if (concreteClass != null) {
            ClassInfo ci = ctx.classTable.get(concreteClass);
            if (ci != null) {
                String methodName = ms.f2.accept(this, ctx);
                MethodInfo mi = ci.lookupMethod(methodName, ctx.classTable);

                if (mi != null) {
                    // Record the resolution context-sensitively
                    InlineContext.CallPath path = new InlineContext.CallPath(ctx.callStack, ms);

                    // Guard against paths becoming polymorphic inside loops
                    String existing = ctx.callSiteResolutions.get(path);
                    if (existing != null && !existing.equals(concreteClass)) {
                        ctx.callSiteResolutions.put(path, "POLYMORPHIC");
                    } else {
                        ctx.callSiteResolutions.put(path, concreteClass);
                    }

                    boolean tryInline = doInline || !ctx.callStack.isEmpty();

                    if (tryInline && !ctx.callSiteResolutions.get(path).equals("POLYMORPHIC")) {
                        // SIMULATE INLINING (Context-Sensitive Flow)
                        List<String> args = collectArgs(ms.f4, ctx);
                        Map<String, String> savedPoints = new LinkedHashMap<>(ctx.pointsTo);
                        Map<String, String> newPoints = new LinkedHashMap<>();

                        for (int i = 0; i < mi.params.size(); i++) {
                            String pname = mi.params.get(i)[1];
                            String arg = (i < args.size()) ? args.get(i) : null;
                            if (arg != null && ctx.pointsTo.containsKey(arg)) {
                                newPoints.put(pname, ctx.pointsTo.get(arg));
                            }
                        }
                        newPoints.put("this", concreteClass);

                        ctx.pointsTo = newPoints;
                        ctx.callStack.push(ms); // Push context

                        // Traverse the callee's statements context-sensitively
                        MethodDeclaration md = mi.astNode;
                        for (Enumeration<Node> e = md.f8.elements(); e.hasMoreElements();) {
                            e.nextElement().accept(this, ctx);
                        }

                        ctx.callStack.pop(); // Pop context
                        ctx.pointsTo = savedPoints; // Restore caller state
                    }
                }
            }
        }
        return null;
    }

    private List<String> collectArgs(NodeOptional argListOpt, InlineContext ctx) {
        List<String> result = new ArrayList<>();
        if (!argListOpt.present())
            return result;
        ArgList al = (ArgList) argListOpt.node;
        result.add(al.f0.accept(this, ctx));
        for (Enumeration<Node> e = al.f1.elements(); e.hasMoreElements();)
            result.add(((ArgRest) e.nextElement()).f1.accept(this, ctx));
        return result;
    }

    // Expressions necessary for evaluation
    @Override
    public String visit(Expression n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(RhsExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(PrimaryExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(AllocationExpression n, InlineContext ctx) {
        return "new " + n.f1.accept(this, ctx) + " ()";
    }

    @Override
    public String visit(Identifier n, InlineContext ctx) {
        return n.f0.tokenImage;
    }

    @Override
    public String visit(ConstOrId n, InlineContext ctx) {
        return n.f0.accept(this, ctx);
    }

    @Override
    public String visit(ThisExpression n, InlineContext ctx) {
        return "this";
    }

    @Override
    public String visit(DotExpression n, InlineContext ctx) {
        return n.f0.accept(this, ctx) + "." + n.f2.accept(this, ctx);
    }
}