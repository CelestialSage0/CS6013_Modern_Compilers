class Need_PureRemoval {
  public static void main(String[] args) {
    int r;
    int unused;
    int fortytwo;
    Helper h;
    h = new Helper();
    fortytwo = 42;
    unused = h.pure(fortytwo);
    r = 100;
    System.out.println(r);
  }
}
class Helper {
  public int pure(int x) {
    int y;
    y = x + x;
    return y;
  }
}
