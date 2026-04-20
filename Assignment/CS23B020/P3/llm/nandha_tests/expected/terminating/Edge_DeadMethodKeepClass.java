class Edge_DeadMethodKeepClass {
  public static void main(String[] args) {
    int r;
    Obj o;
    o = new Obj();
    o.x = 7;
    r = o.x;
    System.out.println(r);
  }
}
class Obj {
  int x;
}
