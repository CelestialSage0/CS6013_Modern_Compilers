class t54 {
  public static void main(String[] args) {
    A $1;
    $1 = new A();
    $1.foo();
    System.out.println(1);
  }
}
class A {
  int f;
  public int foo() {
    A a;
    a = new A();
    a.f = 1;
    return 1;
  }
}
