import syntaxtree.*;
import visitor.*;
import java.io.*;

/**
 * Main driver — BuritoJava to TacoJava compiler.
 *
 * Pipeline:
 * 1. Parse BuritoJava source with the JTB/JavaCC-generated parser.
 * 2. Pass 1: Run SymbolTableVisitor to build the symbol table.
 * 3. Pass 2: Run CodeGenVisitor to emit TacoJava source.
 *
 * Usage:
 * java Main < input.bj > output.tj
 * java Main input.bj (writes to stdout)
 * java Main input.bj out.tj
 */
public class P2 {

  public static void main(String[] args) throws Exception {

    // ---- Determine input / output streams ----
    InputStream in = System.in;
    PrintStream out = System.out;

    if (args.length >= 1) {
      in = new FileInputStream(args[0]);
    }
    if (args.length >= 2) {
      out = new PrintStream(new FileOutputStream(args[1]));
    }

    // ---- Step 1: Parse BuritoJava source ----
    BuritoJavaParser parser = new BuritoJavaParser(in);
    Goal root;
    try {
      root = parser.Goal();
    } catch (ParseException e) {
      System.err.println("[Parse Error] " + e.getMessage());
      System.exit(1);
      return;
    }

    // ---- Step 2: Pass 1 — Build Symbol Table ----
    SymbolTableVisitor stVisitor = new SymbolTableVisitor();
    root.accept(stVisitor, new String[] { null, null });
    SymbolTable symTable = stVisitor.symTable;

    // ---- Step 3: Pass 2 — Generate TacoJava ----
    CodeGenContext ctx = new CodeGenContext(symTable);
    CodeGenVisitor cgVisitor = new CodeGenVisitor();
    root.accept(cgVisitor, ctx);

    // ---- Step 4: Emit output ----
    out.print(ctx.globalOutput.toString());
    out.flush();

    if (args.length >= 2) {
      out.close();
      System.err.println("[Done] TacoJava written to " + args[1]);
    }
  }
}
