class Edge_WhileTrueSideEffect {
  public static void main(String[] args) {
    int x;
    int msg;
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
    msg = 111;
    if (cond) {
      while (t) {
        System.out.println(msg);
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
