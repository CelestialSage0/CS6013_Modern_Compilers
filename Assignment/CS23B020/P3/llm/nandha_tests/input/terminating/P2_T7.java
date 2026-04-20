class T107 {
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
    boolean $4;
    A $5;
    int $6;
    int $7;
    b = new B();
    $3 = 5;
    $2 = b.foobar($3);
    $1 = b.bar($2);
    $0 = !$1;
    while ($0) {
      c = 5;
      $6 = 5;
      $5 = b.foobar($6);
      $4 = b.bar($5);
      $0 = !$4;
    }
    $7 = 1;
    return $7;
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
