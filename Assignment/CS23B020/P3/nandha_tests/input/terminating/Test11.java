// Input

class Test11 {
  public static void main(String[] args) {
    A object;
    int a;
    int b;
    int c;
    object = new A();
    a = 3;
    a = object.foo(a);
    System.out.println(a);
    b = 5;
    b = object.bar(b);
    System.out.println(b);
    c = 4;
    c = object.foo(c);
    System.out.println(c);
  }
}

class A {
  public int foo(int y) {
    A _this;
    int x;
    int z;
    int three;
    boolean b;
    _this = this;
    three = 3;
    x = _this.bar(y);
    b = three < x;
    if (b)
      z = 3;
    else
      z = 4;
    return z;
  }

  public int bar(int y) { return y; }
}