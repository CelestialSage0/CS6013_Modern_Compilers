class Edge_WhileTrueDeadBranch {
  public static void main(String[] args) {
    int x;
    boolean cond;
    boolean t;
    boolean fv;
    Box b;
    b = new Box();
    fv = false;
    b.flag = fv;
    cond = b.flag;
    t = true;
    x = 0;
    if (cond) {
      while (t) {
        x = 1;
      }
    } else {
      x = 42;
    }
    System.out.println(x);
  }
}
class Box {
  boolean flag;
}
