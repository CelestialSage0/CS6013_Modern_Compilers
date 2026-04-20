class Main {
  public static void main(String[] args) {
    int $0;
    Z1 $1;
    $1 = new Z1();
    $0 = $1.foo();
    System.out.println($0);
  }
}
class Z1 {
  public int foo() {
    boolean $0;
    boolean $1;
    boolean $2;
    int $3;
    $1 = this.baz();
    $2 = this.bar();
    if ($1) {
      $0 = $2;
    } else {
      $0 = false;
    }
    if ($0) {
    } else {
    }
    $3 = 10;
    return $3;
  }
  public boolean baz() {
    int $0;
    boolean $1;
    $0 = 1729;
    System.out.println($0);
    $1 = false;
    return $1;
  }
  public boolean bar() {
    int $0;
    boolean $1;
    $0 = 6561;
    System.out.println($0);
    $1 = true;
    return $1;
  }
}
