class Edge_WhileTrueFieldPred {
  public static void main(String[] args) {
    int x;
    boolean cond;
    Flags f;
    f = new Flags();
    f.stop = false;
    cond = f.stop;
    x = 0;
    while (cond) {
      x = x + 1;
      cond = f.stop;
    }
    System.out.println(x);
  }
}
class Flags {
  boolean stop;
}
