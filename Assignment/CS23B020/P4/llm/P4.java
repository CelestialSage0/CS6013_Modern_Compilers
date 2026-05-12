import syntaxtree.*;
import visitor.*;
import java.util.*;

/**
 * P4 – function-inlining pass for FunkyTACoJava.
 *
 * Usage: java P4 < P.java > Pf.java
 */
public class P4 {
    public static void main(String[] args) throws Exception {
        FunkyTacoJavaParser parser = new FunkyTacoJavaParser(System.in);
        Goal root = parser.Goal();

        // Pass 1: Build class table
        Map<String, ClassInfo> classTable = new LinkedHashMap<>();
        BuildTableVisitor btv = new BuildTableVisitor();
        btv.visit(root, classTable);

        InlineContext ctx = new InlineContext(classTable);

        // Pass 2: Points-To Analysis of the full code
        // Populates ctx.callSiteResolutions with monomorphic targets
        PointsToVisitor ptv = new PointsToVisitor();
        ptv.visit(root, ctx);

        // Pass 3: Inlining and Pretty-Printing
        // Uses the pre-computed resolutions from Pass 2
        InlineVisitor iv = new InlineVisitor();
        iv.visit(root, ctx);

        System.out.print(ctx.sb.toString());
    }
}