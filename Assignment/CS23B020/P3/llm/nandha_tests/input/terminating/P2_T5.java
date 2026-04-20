class T105 {
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
    B b;
    int c;
    boolean $0;
    boolean $1;
    A $2;
    int $3;
    b = new B();
    $3 = 5;
    $2 = b.foobar($3);
    $1 = b.bar($2);
    $0 = !$1;
    if ($0) {
      c = 5;
    } else {
      c = 3;
    }
    return c;
  }
}
class B {
  public boolean bar(A a) {
    boolean $0;
    $0 = false;
    return $0;
  }
  public A foobar(int x) {
    A a;
    a = new A();
    return a;
  }
}
