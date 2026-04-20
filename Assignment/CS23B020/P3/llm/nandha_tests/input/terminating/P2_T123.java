class T24 {
  public static void main(String[] args) {
    int $0;
    A $1;
    int $2;
    $1 = new A();
    $2 = 5;
    $0 = $1.foo($2);
    System.out.println($0);
  }
}
class A {
  public int foo(int p1) {
    B b;
    int x;
    boolean y;
    int $0;
    y = true;
    b = new B();
    x = b.bar(y);
    $0 = 1;
    return $0;
  }
}
class B {
  public int bar(boolean p1) {
    int $0;
    $0 = 1;
    return $0;
  }
}
