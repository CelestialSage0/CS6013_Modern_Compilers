// Input

class Test12 {
  public static void main(String[] args) {
    A object;
    B bobject;
    int a;
    int b;
    object = new A();
    a = 67;
    a = object.foo1(a);
    System.out.println(a);
    bobject = new B();
    b = 67;
    b = bobject.foo1(b);
    System.out.println(b);
  }
}

class A {
  public int foo1(int x) {
    A _this;
    int y;
    _this = this;
    y = x * x;
    System.out.println(y);
    y = _this.foo2(y);
    System.out.println(y);
    return y;
  }

  public int foo2(int x) {
    A _this;
    int y;
    _this = this;
    y = x + x;
    System.out.println(y);
    y = _this.foo3(y);
    System.out.println(y);
    return y;
  }

  public int foo3(int x) {
    A _this;
    int y;
    _this = this;
    y = x * x;
    System.out.println(y);
    return y;
  }
}

class B {
  public int foo1(int x) {
    B _this;
    int y;
    _this = this;
    y = x * x;
    y = _this.foo2(y);
    return y;
  }

  public int foo2(int x) {
    B _this;
    int y;
    _this = this;
    y = x + x;
    y = _this.foo3(y);
    return y;
  }

  public int foo3(int x) {
    B _this;
    int y;
    _this = this;
    y = x * x;
    return y;
  }
}