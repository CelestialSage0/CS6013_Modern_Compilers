class T210 {
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
    boolean b1;
    A a;
    int $0;
    int $1;
    a = new A();
    $0 = 2;
    b1 = a.bar($0);
    $1 = 1;
    return $1;
  }
  public boolean bar(int p) {
    boolean $0;
    $0 = this.baz(p);
    return $0;
  }
  public boolean baz(int p) {
    boolean $0;
    $0 = true;
    return $0;
  }
}
