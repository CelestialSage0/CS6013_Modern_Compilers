class Edge_RecursiveOverwriteSOF {
  public static void main(String[] args) {
    int r;
    int five;
    Obj o;
    o = new Obj();
    five = 5;
    r = o.foo(five);
    System.out.println(r);
  }
}
class Obj {
  boolean flag;
  public int foo(int x) {
    int a;
    int two;
    two = 2;
    flag = false;
    if (flag) {
      a = 0;
    } else {
      a = this.foo(x);
    }
    a = two;
    return a;
  }
}
