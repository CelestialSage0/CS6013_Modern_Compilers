// Input

class Test10 {
  public static void main(String[] args) {
    A object;
    int a;
    int b;
    int c;
    object = new A();
    a = 123;
    a = object.foo(a);
    System.out.println(a);
    b = a * a;
    c = b;
    a = a + a;
    System.out.println(a);
  }
}

class A {
  public int foo(int x) {
    A _this;
    int z;
    _this = this;
    z = _this.bar(x);
    return z;
  }

  public int bar(int y) {
    int z;
    z = y * y;
    return z;
  }
}