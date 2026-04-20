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
  int z;
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
    $3 = y;
    $5 = z;
    $4 = this.intID($5);
    $2 = $3 + $4;
    $1 = this.intID($2);
    $8 = z;
    $10 = y;
    $9 = this.intID($10);
    $7 = $8 + $9;
    $6 = this.intID($7);
    $0 = this.add($1, $6);
    return $0;
  }
  public int add(int x, int y) {
    int $0;
    $0 = x + y;
    return $0;
  }
  public int intID(int x) {
    int $0;
    int $1;
    int $2;
    int $3;
    int $4;
    int $5;
    $0 = y;
    $1 = 1;
    y = $0 + $1;
    $2 = z;
    $3 = 2;
    z = $2 + $3;
    System.out.println(x);
    $4 = y;
    System.out.println($4);
    $5 = z;
    System.out.println($5);
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
}
