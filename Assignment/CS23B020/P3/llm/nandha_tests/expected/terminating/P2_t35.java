class t35 {
  public static void main(String[] args) {
    A $1;
    $1 = new A();
    $1.foo();
    System.out.println(0);
  }
}
class A {
  public int foo() {
    int a;
    int b;
    boolean $0;
    boolean $2;
    a = 0;
    $0 = true;
    while ($0) {
      b = 0;
      $2 = true;
      while ($2) {
        System.out.println(b);
        b = b + 1;
        $2 = b < 10;
      }
      a = a + 1;
      $0 = a < 10;
    }
    return 0;
  }
}
