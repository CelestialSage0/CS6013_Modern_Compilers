import syntaxtree.Goal;
import visitor.*;

/**
 * P3 — Optimizing compiler front-end for TACoJava -> TACoJava2.
 *
 * Pipeline:
 * stdin -> Parser -> AST
 * |
 * ClassHierarchyVisitor (builds class/method hierarchy)
 * |
 * SymbolTableVisitor (builds type info per scope)
 * |
 * CallGraphVisitor (CHA reachability from main)
 * |
 * ConstPropVisitor (conditional constant propagation)
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

        // ---- Parse ------------------------------------------------
        Goal root = new TACoJavaParser(System.in).Goal();

        // ---- Pass 1: Class hierarchy ------------------------------
        ClassHierarchy ch = new ClassHierarchy();
        root.accept(ch, null);

        // ---- Pass 2: Symbol table ---------------------------------
        SymbolTable st = new SymbolTable(ch);
        root.accept(st, null);

        // ---- Pass 3: Call graph via CHA ---------------------------
        // We need the main class name to seed the worklist.
        // The main class is the first class in the program; JTB gives
        // us the Goal node whose f0 is MainClass whose f1 is Identifier.
        String mainClassName = root.f0.f1.f0.tokenImage;

        CallGraph cg = new CallGraph(ch, st, mainClassName);
        root.accept(cg, null);

        // ---- Pass 4: Constant propagation -------------------------
        ConstPropVisitor cpv = new ConstPropVisitor(ch, st, cg);
        root.accept(cpv, null);

        // ---- Pass 5: Dead code marking ----------------------------
        DeadCodeVisitor dc = new DeadCodeVisitor(ch, st, cg, cpv);
        root.accept(dc, null);

        // ---- Pass 6: Code generation -> stdout --------------------
        CodeGen gen = new CodeGen(ch, st, cpv, dc);
        root.accept(gen, null);
        gen.flush();
    }
}