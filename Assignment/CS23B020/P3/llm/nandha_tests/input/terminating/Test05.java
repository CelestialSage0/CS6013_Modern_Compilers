// Input

class Test05 {
  public static void main(String[] args) {
    A object;
    int a;
    object = new A();
    a = 0;
    a = object.foo(a);
    System.out.println(a);
    a = 1;
    a = object.bar(a);
    System.out.println(a);
  }
}

class A {
  public int foo(int x) {
    int zero;
    int one;
    A _this;
    int a;
    int y;
    boolean b;
    _this = this;
    zero = 0;
    one = 1;
    b = zero < x;
    y = x - one;
    a = 0;
    if (b) {
      a = _this.foo(y);
      a = a + one;
      a = a + one;
    } else {
    }
    return a;
  }

  public int bar(int x) {
    int zero;
    int one;
    A _this;
    int a;
    int y;
    boolean b;
    _this = this;
    zero = 0;
    one = 1;
    b = zero < x;
    y = x - one;
    a = 0;
    if (b) {
      a = _this.bar(y);
      a = a + one;
      a = a + one;
    } else {
    }
    return a;
  }
}
