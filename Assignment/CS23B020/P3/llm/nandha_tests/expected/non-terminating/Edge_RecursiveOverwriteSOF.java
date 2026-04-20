class Edge_RecursiveOverwriteSOF {
  public static void main(String[] args) {
    Obj o;
    o = new Obj();
    o.foo(5);
    System.out.println(2);
  }
}
class Obj {
  boolean flag;
  public int foo(int x) {
    flag = false;
    if (flag) {
    } else {
      this.foo(5);
    }
    return 2;
  }
}
