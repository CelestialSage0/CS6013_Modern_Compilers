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
    int $1;
    int $2;
    int $3;
    a = 0;
    $1 = 10;
    $0 = a < $1;
    while ($0) {
      System.out.println(a);
      $2 = 1;
      a = a + $2;
      $3 = 10;
      $0 = a < $3;
    }
    b = a;
    return b;
  }
}
