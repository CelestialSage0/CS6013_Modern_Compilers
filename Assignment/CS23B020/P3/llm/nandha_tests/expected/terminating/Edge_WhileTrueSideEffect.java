class Edge_WhileTrueSideEffect {
  public static void main(String[] args) {
    boolean cond;
    boolean t;
    Box b;
    b = new Box();
    b.flag = false;
    cond = b.flag;
    t = true;
    if (cond) {
      while (t) {
        System.out.println(111);
      }
    } else {
    }
    System.out.println(42);
  }
}
class Box {
  boolean flag;
}
