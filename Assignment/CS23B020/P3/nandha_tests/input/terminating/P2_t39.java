class t39 {
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
    int c;
    boolean $0;
    int $1;
    boolean $2;
    int $3;
    int $4;
    int $5;
    boolean $6;
    int $7;
    int $8;
    int $9;
    int $10;
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
      c = 0;
      $6 = c < b;
      while ($6) {
        System.out.println(c);
        $7 = 1;
        c = c + $7;
        $6 = c < b;
      }
      $8 = 1;
      a = a + $8;
      $9 = 10;
      $0 = a < $9;
    }
    $10 = 0;
    return $10;
  }
}
