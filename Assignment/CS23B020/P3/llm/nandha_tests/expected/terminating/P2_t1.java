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
    boolean $3;
    int $5;
    int $7;
    boolean $9;
    boolean $10;
    boolean $12;
    int $14;
    int $16;
    int $18;
    $1 = this.boolID(false);
    $3 = this.boolID(true);
    if ($1) {
      $0 = $3;
    } else {
      $0 = false;
    }
    if ($0) {
      $5 = this.intID(1000);
      System.out.println($5);
    } else {
      $7 = this.intID(1001);
      System.out.println($7);
    }
    $10 = this.boolID(true);
    $12 = this.boolID(false);
    if ($10) {
      $9 = true;
    } else {
      $9 = $12;
    }
    if ($9) {
      $14 = this.intID(1002);
      System.out.println($14);
    } else {
      $16 = this.intID(1003);
      System.out.println($16);
    }
    $18 = this.intID(2000);
    return $18;
  }
  public int intID(int x) {
    int $0;
    int $2;
    $0 = y;
    y = $0 + 1;
    System.out.println(x);
    $2 = y;
    System.out.println($2);
    return x;
  }
  public boolean boolID(boolean x) {
    int $0;
    int $4;
    $0 = y;
    y = $0 + 1;
    if (x) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    $4 = y;
    System.out.println($4);
    return x;
  }
}
