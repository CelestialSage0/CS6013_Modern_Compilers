class Edge_DeadMethodKeepClass {
  public static void main(String[] args) {
    int r;
    Obj o;
    int seven;
    o = new Obj();
    seven = 7;
    o.x = seven;
    r = o.x;
    System.out.println(r);
  }
}
class Obj {
  int x;
  public int unused() {
    int y;
    y = 999;
    System.out.println(y);
    return y;
  }
}
