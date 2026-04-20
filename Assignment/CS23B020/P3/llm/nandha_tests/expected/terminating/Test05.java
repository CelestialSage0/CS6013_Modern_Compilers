class Test05 {
  public static void main(String[] args) {
    A object;
    int a;
    object = new A();
    System.out.println(0);
    a = object.bar(1);
    System.out.println(a);
  }
}
class A {
  public int bar(int x) {
    A _this;
    int a;
    int y;
    boolean b;
    _this = this;
    b = 0 < x;
    y = x - 1;
    a = 0;
    if (b) {
      a = _this.bar(y);
      a = a + 1;
      a = a + 1;
    } else {
    }
    return a;
  }
}
