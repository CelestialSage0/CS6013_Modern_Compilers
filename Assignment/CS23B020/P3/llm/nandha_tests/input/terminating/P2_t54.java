class t54 {
  public static void main(String[] args) {
    int $0;
    A $1;
    $1 = new A();
    $0 = $1.foo();
    System.out.println($0);
  }
}
class A {
  int f;
  public int foo() {
    A a;
    int b;
    int $0;
    a = new A();
    b = 1;
    $0 = 1;
    a.f = $0;
    return b;
  }
}
