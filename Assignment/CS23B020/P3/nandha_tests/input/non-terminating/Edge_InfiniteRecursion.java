class Edge_InfiniteRecursion {
  public static void main(String[] args) {
    int result;
    int dead;
    int one;
    Foo f;
    one = 1;
    f = new Foo();
    result = f.spin(one);
    dead = 999;
    System.out.println(dead);
  }
}
class Foo {
  public int spin(int x) {
    int r;
    System.out.println(x);
    r = this.spin(x);
    return r;
  }
}
