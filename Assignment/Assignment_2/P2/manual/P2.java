import syntaxtree.*;
import visitor.*;

public class P2 {
    public static void main(String[] args) {
        try {
            Node root = new BuritoJavaParser(System.in).Goal();

            SymbolTableBuilder st = new SymbolTableBuilder();
            root.accept(st, null);

            GJDepthFirst visitor = new GJDepthFirst(st.ST);
            root.accept(visitor, null);

        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }
}