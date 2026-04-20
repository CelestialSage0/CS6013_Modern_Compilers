class T31 {
  public static void main(String[] args) {
    int $0;
    A $1;
    int $2;
    boolean $3;
    $1 = new A();
    $2 = 1;
    $3 = true;
    $0 = $1.foo($2, $3);
    System.out.println($0);
  }
}
class A {
  public int foo(int p1, boolean p2) {
    A a;
    B b;
    int $0;
    a = new A();
    b = new B();
    $0 = 2;
    p1 = b.bar(a, $0);
    return p1;
  }
}
class B {
  public int bar(A p1, int p2) { return p2; }
}
