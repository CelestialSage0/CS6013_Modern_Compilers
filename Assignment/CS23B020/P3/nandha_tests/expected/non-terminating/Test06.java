class Test06 {
  public static void main(String[] args) {
    A object;
    int a;
    object = new A();
    a = object.fubar(0);
    System.out.println(a);
  }
}
class A {
  public int fubar(int x) {
    A _this;
    int a;
    _this = this;
    a = _this.fubar(0);
    return a;
  }
}
