class T104 {
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
    boolean $0;
    boolean $1;
    boolean $2;
    int $3;
    a = new A();
    b = new B();
    $1 = b.bar(a);
    $0 = !$1;
    while ($0) {
      c = 5;
      $2 = b.bar(a);
      $0 = !$2;
    }
    $3 = 1;
    return $3;
  }
}
class B {
  public boolean bar(A a) {
    boolean $0;
    $0 = true;
    return $0;
  }
  public A foobar(int x) {
    A a;
    a = new A();
    return a;
  }
}
