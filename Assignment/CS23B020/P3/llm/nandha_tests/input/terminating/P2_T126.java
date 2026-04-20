class T27 {
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
    boolean $0;
    int $1;
    b = new B();
    $0 = true;
    x = b.bar($0);
    $1 = 1;
    return $1;
  }
}
class B extends A {
  public int bar(boolean p1) {
    A a;
    B b;
    int $0;
    a = new A();
    a = new B();
    b = new B();
    $0 = 1;
    return $0;
  }
}
