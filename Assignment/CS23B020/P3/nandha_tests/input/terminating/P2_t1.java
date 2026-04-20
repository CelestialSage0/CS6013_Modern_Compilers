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
    boolean $0;
    boolean $1;
    boolean $2;
    boolean $3;
    boolean $4;
    int $5;
    int $6;
    int $7;
    int $8;
    boolean $9;
    boolean $10;
    boolean $11;
    boolean $12;
    boolean $13;
    int $14;
    int $15;
    int $16;
    int $17;
    int $18;
    int $19;
    $2 = false;
    $1 = this.boolID($2);
    $4 = true;
    $3 = this.boolID($4);
    if ($1) {
      $0 = $3;
    } else {
      $0 = false;
    }
    if ($0) {
      $6 = 1000;
      $5 = this.intID($6);
      System.out.println($5);
    } else {
      $8 = 1001;
      $7 = this.intID($8);
      System.out.println($7);
    }
    $11 = true;
    $10 = this.boolID($11);
    $13 = false;
    $12 = this.boolID($13);
    if ($10) {
      $9 = true;
    } else {
      $9 = $12;
    }
    if ($9) {
      $15 = 1002;
      $14 = this.intID($15);
      System.out.println($14);
    } else {
      $17 = 1003;
      $16 = this.intID($17);
      System.out.println($16);
    }
    $19 = 2000;
    $18 = this.intID($19);
    return $18;
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
