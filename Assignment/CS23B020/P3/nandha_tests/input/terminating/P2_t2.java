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
    int i;
    int j;
    int $0;
    int $1;
    boolean $2;
    int $3;
    int $4;
    int $5;
    int $6;
    int $7;
    int $8;
    int $9;
    int $10;
    int $11;
    int $12;
    int $13;
    $0 = 1;
    j = this.intID($0);
    $1 = 1;
    i = this.intID($1);
    $3 = this.intID(i);
    $5 = 10;
    $4 = this.intID($5);
    $2 = $3 < $4;
    while ($2) {
      $6 = this.intID(j);
      $7 = this.intID(i);
      j = $6 * $7;
      $8 = this.intID(i);
      $10 = 1;
      $9 = this.intID($10);
      i = $8 + $9;
      $11 = this.intID(i);
      $13 = 10;
      $12 = this.intID($13);
      $2 = $11 < $12;
    }
    return j;
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
}
