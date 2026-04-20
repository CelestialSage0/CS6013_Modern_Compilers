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
    A a;
    int $0;
    a = new B();
    $0 = 1;
    return $0;
  }
}
class B extends A {}
