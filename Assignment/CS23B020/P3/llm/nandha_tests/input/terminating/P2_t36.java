class t36 {
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
    boolean $2;
    int $3;
    int $4;
    int $5;
    int $6;
    int $7;
    int $8;
    a = 0;
    $1 = 10;
    $0 = a < $1;
    while ($0) {
      b = 0;
      $3 = 10;
      $2 = b < $3;
      while ($2) {
        System.out.println(b);
        $4 = 1;
        b = b + $4;
        $5 = 10;
        $2 = b < $5;
      }
      $6 = 1;
      a = b + $6;
      $7 = 10;
      $0 = a < $7;
    }
    $8 = 0;
    return $8;
  }
}
