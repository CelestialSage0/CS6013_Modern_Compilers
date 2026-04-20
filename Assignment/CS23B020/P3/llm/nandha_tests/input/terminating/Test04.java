// Input

class Test04 {
  public static void main(String[] args) {
    A object;
    int a;
    int two;
    int three;
    two = 2;
    three = 3;
    object = new A();
    a = object.foo(two, three);
    System.out.println(a);
    a = object.bar(two, three);
    System.out.println(a);
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
    s = true;
    _this = this;
    _this.b = s;
    s = _this.b;
    if (s) {
      p = x;
      q = y;
    } else {
      p = y;
      q = x;
    }
    r = p + q;
    return r;
  }

  public int bar(int x, int y) {
    int p;
    int q;
    int r;
    boolean s;
    A _this;
    s = false;
    _this = this;
    _this.b = s;
    s = _this.b;
    if (s) {
      p = x;
      q = y;
      r = p + q;
    } else {
      p = y;
      q = x;
      r = p + q;
    }
    return r;
  }
}
