class Edge_WhileTrueFieldPred {
  public static void main(String[] args) {
    int x;
    int one;
    boolean cond;
    boolean fv;
    Flags f;
    f = new Flags();
    fv = false;
    f.stop = fv;
    cond = f.stop;
    one = 1;
    x = 0;
    while (cond) {
      x = x + one;
      cond = f.stop;
    }
    System.out.println(x);
  }
}
class Flags {
  boolean stop;
}
