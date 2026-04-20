class Edge_WhileTrueUnreachAfter {
  public static void main(String[] args) {
    int x;
    int y;
    int dead;
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
    dead = 999;
    if (cond) {
      while (t) {
        x = 1;
      }
      System.out.println(dead);
    } else {
      x = 42;
    }
    System.out.println(x);
  }
}
class Box {
  boolean flag;
}
