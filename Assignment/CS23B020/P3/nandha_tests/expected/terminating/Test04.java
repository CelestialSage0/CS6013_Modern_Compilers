class Test04 {
  public static void main(String[] args) {
    A object;
    int a;
    object = new A();
    a = object.foo(2, 3);
    System.out.println(a);
    object.bar(2, 3);
    System.out.println(5);
  }
}
class A {
  boolean b;
  public int foo(int x, int y) {
    int p;
    int q;
    int r;
    boolean s;
    A _this;
    _this = this;
    _this.b = true;
    s = _this.b;
    if (s) {
      p = 2;
      q = 3;
    } else {
      p = 3;
      q = 2;
    }
    r = p + q;
    return r;
  }
  public int bar(int x, int y) {
    A _this;
    _this = this;
    _this.b = false;
    return 5;
  }
}
