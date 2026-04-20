class Main {
  public static void main(String[] args) {
    int $0;
    Foo $1;
    $1 = new Foo();
    $0 = $1.foo();
    System.out.println($0);
  }
}
class Foo {
  public int foo() {
    int $0;
    int $1;
    int $2;
    int $3;
    int $4;
    int $5;
    int $6;
    int $7;
    int $8;
    int $9;
    int $10;
    $3 = this.bar();
    $4 = this.baz();
    $2 = $3 + $4;
    $6 = this.baz();
    $7 = this.bar();
    $5 = $6 - $7;
    $1 = $2 - $5;
    $9 = this.baz();
    $10 = this.bar();
    $8 = this.foobaz($9, $10);
    $0 = this.foobar($1, $8);
    return $0;
  }
  public int bar() {
    int $0;
    int $1;
    $0 = 1;
    System.out.println($0);
    $1 = 1;
    return $1;
  }
  public int baz() {
    int $0;
    int $1;
    $0 = 2;
    System.out.println($0);
    $1 = 2;
    return $1;
  }
  public int foobar(int x, int y) {
    int $0;
    int $1;
    int $2;
    int $3;
    $1 = x + y;
    $2 = 234;
    $0 = $1 + $2;
    System.out.println($0);
    $3 = x + y;
    return $3;
  }
  public int foobaz(int x, int y) {
    int $0;
    int $1;
    int $2;
    int $3;
    $1 = x * y;
    $2 = 213;
    $0 = $1 * $2;
    System.out.println($0);
    $3 = x * y;
    return $3;
  }
}
