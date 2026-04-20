class Test03 {
  public static void main(String[] args) {
    A object;
    boolean b;
    object = new A();
    object.choice();
    System.out.println(10);
    object.choice();
    System.out.println(50);
    b = object.choice();
    if (b) {
      object.choice();
    } else {
      object.choice();
    }
    System.out.println(1000);
  }
}
class A {
  int ctr;
  public boolean choice() {
    A _this;
    int x;
    int temp;
    boolean cond;
    _this = this;
    temp = _this.ctr;
    temp = temp + 1;
    _this.ctr = temp;
    x = _this.ctr;
    cond = 0 < x;
    while (cond) {
      x = x - 3;
      cond = 0 < x;
    }
    cond = x < 0;
    cond = !cond;
    return cond;
  }
}
