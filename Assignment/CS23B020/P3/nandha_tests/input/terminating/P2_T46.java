class T146 {
  public static void main(String[] args) {
    int $0;
    A $1;
    int $2;
    $1 = new A();
    $2 = 2;
    $0 = $1.foo($2);
    System.out.println($0);
  }
}
class A {
  public int foo(int p1) {
    int b1;
    A a;
    A $0;
    int $1;
    a = new A();
    $0 = this.bar();
    $1 = 2;
    b1 = $0.baz($1);
    return b1;
  }
  public A bar() {
    A $0;
    $0 = new A();
    return $0;
  }
  public int baz(int x) { return x; }
}
