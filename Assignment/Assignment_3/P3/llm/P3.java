import syntaxtree.*;
import visitor.*;

/**
 * P3 — Optimizing compiler: TACoJava -> TACoJava2
 *
 * Pipeline:
 * stdin -> Parser -> AST
 * |
 * ClassHierarchyVisitor (hierarchy + CHA support)
 * |
 * SymbolTableVisitor (type info per scope)
 * |
 * CallGraphVisitor (CHA reachability from main)
 * |
 * ConstPropVisitor (initial intraprocedural CP, params=NAC)
 * |
 * InterConstPropVisitor (interprocedural: collect call-site args,
 * inject into params, re-run CP to fixpoint)
 * |
 * DeadCodeVisitor (liveness + dead-code marking)
 * |
 * CodeGenVisitor (emit TACoJava2 to stdout)
 *
 * Usage:
 * java P3 < P.java > Pc.java
 */
public class P3 {
    public static void main(String[] args) throws Exception {

        // Parse
        Goal root = new TACoJavaParser(System.in).Goal();

        // Pass 1: class hierarchy
        ClassHierarchy ch = new ClassHierarchy();
        root.accept(ch, null);

        // Pass 2: symbol table
        SymbolTable st = new SymbolTable(ch);
        root.accept(st, null);

        // Pass 3: call graph (CHA)
        String mainClassName = root.f0.f1.f0.tokenImage;
        CallGraph cg = new CallGraph(ch, st, mainClassName);
        root.accept(cg, null);

        // Pass 4a: initial intraprocedural CP (params start as NAC)
        ConstPropVisitor cpv = new ConstPropVisitor(ch, st, cg);
        root.accept(cpv, null);

        // Pass 4b: interprocedural CP — refine params from call-site args
        InterConstPropVisitor icp = new InterConstPropVisitor(ch, st, cg, cpv);
        icp.run(root);

        // Pass 5: dead code marking
        DeadCodeVisitor dc = new DeadCodeVisitor(ch, st, cg, cpv);
        root.accept(dc, null);

        // Pass 6: code generation
        CodeGen gen = new CodeGen(ch, st, cpv, dc);
        root.accept(gen, null);
        gen.flush();
    }
}