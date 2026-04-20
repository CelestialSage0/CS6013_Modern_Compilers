class Test11 {
  public static void main(String[] args) {
    A object;
    int a;
    int b;
    int c;
    object = new A();
    a = object.foo(3);
    System.out.println(a);
    b = object.bar(5);
    System.out.println(b);
    c = object.foo(4);
    System.out.println(c);
  }
}
class A {
  public int foo(int y) {
    A _this;
    int x;
    int z;
    boolean b;
    _this = this;
    x = _this.bar(y);
    b = 3 < x;
    if (b) {
      z = 3;
    } else {
      z = 4;
    }
    return z;
  }
  public int bar(int y) { return y; }
}
