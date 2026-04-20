class T256 {
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
    C c;
    boolean b;
    int $0;
    c = new C();
    b = c.bar();
    $0 = 1;
    return $0;
  }
  public boolean bar() {
    boolean $0;
    $0 = true;
    return $0;
  }
}
class B extends A {}
class C extends B {}
