// Input

class Test03 {
  public static void main(String[] args) {
    A object;
    int a;
    boolean b;
    object = new A();
    b = object.choice();
    b = true;
    a = 10;
    if (b) {
    } else {
      a = 5;
    }
    System.out.println(a);
    b = object.choice();
    b = false;
    a = 100;
    if (b) {
    } else {
      a = 50;
    }
    System.out.println(a);
    b = object.choice();
    if (b) {
      b = object.choice();
      if (b) {
        a = 1000;
      } else {
        a = 1000;
      }
    } else {
      b = object.choice();
      if (b) {
        a = 1000;
      } else {
        a = 1000;
      }
    }
    System.out.println(a);
  }
}

class A {
  int ctr;

  public boolean choice() {
    A _this;
    int x;
    int zero;
    int one;
    int three;
    int temp;
    boolean cond;
    _this = this;
    zero = 0;
    one = 1;
    three = 3;
    temp = _this.ctr;
    temp = temp + one;
    _this.ctr = temp;
    x = _this.ctr;
    cond = zero < x;
    while (cond) {
      x = x - three;
      cond = zero < x;
    }
    cond = x < zero;
    cond = !cond;
    return cond;
  }
}