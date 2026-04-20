class CHA_AllSameReturn {
  public static void main(String[] args) {
    int r;
    Base b;
    b = new Child1();
    r = b.foo();
    System.out.println(r);
  }
}
class Base {
  public int foo() {
    int x;
    x = 99;
    return x;
  }
}
class Child1 extends Base {
  public int foo() {
    int x;
    x = 99;
    return x;
  }
}
class Child2 extends Base {
  public int foo() {
    int x;
    x = 99;
    return x;
  }
}
