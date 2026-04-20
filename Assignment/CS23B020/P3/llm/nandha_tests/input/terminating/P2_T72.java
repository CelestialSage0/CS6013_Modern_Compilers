class T214 {
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
    A $0;
    int $1;
    int $2;
    a = new A();
    b = new B();
    $1 = 2;
    $0 = a.foobar($1);
    c = this.bar(a, $0);
    $2 = 1;
    return $2;
  }
  public int bar(A p1, A p2) {
    int $0;
    $0 = 2;
    return $0;
  }
  public A foobar(int p1) {
    A $0;
    $0 = new A();
    return $0;
  }
}
class B extends A {}
