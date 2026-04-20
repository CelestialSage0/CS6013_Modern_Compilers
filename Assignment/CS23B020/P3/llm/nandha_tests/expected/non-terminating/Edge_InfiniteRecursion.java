class Edge_InfiniteRecursion {
  public static void main(String[] args) {
    Foo f;
    f = new Foo();
    f.spin(1);
    System.out.println(999);
  }
}
class Foo {
  public int spin(int x) {
    int r;
    System.out.println(1);
    r = this.spin(1);
    return r;
  }
}
