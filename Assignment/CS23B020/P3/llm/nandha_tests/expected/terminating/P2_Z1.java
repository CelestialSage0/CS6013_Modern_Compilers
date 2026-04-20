class Main {
  public static void main(String[] args) {
    Z1 $1;
    $1 = new Z1();
    $1.foo();
    System.out.println(10);
  }
}
class Z1 {
  public int foo() {
    this.baz();
    this.bar();
    return 10;
  }
  public boolean baz() {
    System.out.println(1729);
    return false;
  }
  public boolean bar() {
    System.out.println(6561);
    return true;
  }
}
