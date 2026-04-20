class Main {
  public static void main(String[] args) {
    int $0;
    t $1;
    $1 = new t();
    $0 = $1.foo();
    System.out.println($0);
  }
}
class t {
  int y;
  public int foo() {
    t x;
    int y;
    t $0;
    t $1;
    t $2;
    int $3;
    x = new t();
    $2 = this.tID(x);
    $1 = $2.tID(x);
    $0 = $1.tID(x);
    $3 = 3;
    y = $0.intID($3);
    return y;
  }
  public int intID(int x) {
    int $0;
    int $1;
    int $2;
    $0 = y;
    $1 = 1;
    y = $0 + $1;
    System.out.println(x);
    $2 = y;
    System.out.println($2);
    return x;
  }
  public boolean boolID(boolean x) {
    int $0;
    int $1;
    int $2;
    int $3;
    int $4;
    $0 = y;
    $1 = 1;
    y = $0 + $1;
    if (x) {
      $2 = 1;
      System.out.println($2);
    } else {
      $3 = 0;
      System.out.println($3);
    }
    $4 = y;
    System.out.println($4);
    return x;
  }
  public t tID(t x) {
    int $0;
    int $1;
    int $2;
    $0 = y;
    $1 = 1;
    y = $0 + $1;
    $2 = y;
    System.out.println($2);
    return x;
  }
}
