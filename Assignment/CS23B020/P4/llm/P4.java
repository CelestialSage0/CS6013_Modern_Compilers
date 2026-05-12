import syntaxtree.*;
import visitor.*;
import java.io.*;
import java.util.*;

/**
 * P4: Function Inlining for FunkyTACoJava
 *
 * Usage: java P4 < P.java > Pf.java
 *
 * Pipeline:
 * 1. Parse stdin into AST (FunkyTacoJavaParser / JTB)
 * 2. SymbolTableBuilder - build symbol tables
 * 3. TypeFlowVisitor - points-to analysis, identify monomorphic INLINE sites
 * 4. InlineTransformVisitor - pretty-print with inlining
 */
public class P4 {
    public static void main(String[] args) throws Exception {
        // Parse from stdin
        FunkyTacoJavaParser parser = new FunkyTacoJavaParser(System.in);
        Goal root = parser.Goal();

        // Pass 1: build symbol tables
        SymbolTableBuilder symtab = new SymbolTableBuilder();
        root.accept(symtab, null);

        // Pass 2: points-to analysis
        TypeFlowVisitor typeFlow = new TypeFlowVisitor(symtab);
        root.accept(typeFlow, new java.util.HashMap<>());

        // Pass 3: pretty-print with inlining
        InlineTransformVisitor transform = new InlineTransformVisitor(
                System.out, symtab, typeFlow.inlineTargets);
        root.accept(transform, null);
    }
}