class t31 {
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
    int a;
    int b;
    boolean $0;
    a = 0;
    $0 = true;
    while ($0) {
      System.out.println(a);
      a = a + 1;
      $0 = a < 10;
    }
    b = a;
    return b;
  }
}
