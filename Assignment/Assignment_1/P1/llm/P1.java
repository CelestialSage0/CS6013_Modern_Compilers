import syntaxtree.*;
import visitor.*;

public class P1 {
  public static void main(String[] args) {
    try {
      Node root = new BuritoJavaParser(System.in).Goal();
      LLM<Object, Object> checker = new LLM<Object, Object>();
      root.accept(checker, null);

      if (checker.hasFinalAssignment()) {
        System.out.println("Final variable being assigned.");
      } else if (checker.hasUninitializedUse()) {
        System.out.println("Uninitialized variable found.");
      } else {
        System.out.println("No issue with variables.");
      }
    } catch (ParseException e) {
      System.err.println("Parse error: " + e.getMessage());
      System.exit(1);
    }
  }
}
