class T22 {
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
    B b;
    int x;
    int y;
    boolean z;
    x = 0;
    y = 0;
    z = false;
    b = new B();
    p1 = b.bar(x, y, z);
    return p1;
  }
}
class B {
  public int bar(int p1, int p2, boolean p3) { return p1; }
}
