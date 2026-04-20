// Input

class Test06 {
  public static void main(String[] args) {
    A object;
    int a;
    object = new A();
    a = 0;
    a = object.fubar(a);
    System.out.println(a);
  }
}

class A {
  public int fubar(int x) {
    int zero;
    A _this;
    int a;
    boolean b;
    _this = this;
    zero = 0;
    b = zero < x;
    if (b) {
      a = 0;
    } else {
      a = _this.fubar(x);
    }
    return a;
  }
}
