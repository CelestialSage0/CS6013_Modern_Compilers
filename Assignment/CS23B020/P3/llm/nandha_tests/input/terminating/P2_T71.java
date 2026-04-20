class T213 {
  public static void main(String[] args) {
    int $0;
    A $1;
    $1 = new A();
    $0 = $1.foo();
    System.out.println($0);
  }
}
class A {
  public int foo() {
    A a;
    B b;
    int c;
    int $0;
    a = new A();
    b = new B();
    c = this.bar(a, b);
    $0 = 1;
    return $0;
  }
  public int bar(A p1, A p2) {
    int $0;
    $0 = 2;
    return $0;
  }
}
class B extends A {}
