class T3 {
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
    B a;
    A b;
    int $0;
    a = new B();
    b = a;
    $0 = 1;
    return $0;
  }
}
class B extends A {}
