class Test02 {
  public static void main(String[] args) {
    int $0;
    Driver $1;
    $1 = new Driver();
    $0 = $1.run();
    System.out.println($0);
  }
}
class Driver {
  int seed;
  public int run() {
    int i;
    boolean $0;
    int $3;
    int $5;
    int $7;
    i = 67;
    $0 = true;
    while ($0) {
      System.out.println(i);
      $3 = i * 2;
      i = this.random($3);
      $5 = i * i;
      $0 = $5 < 1600000000;
    }
    $7 = this.random(i);
    return $7;
  }
  public int random(int offset) {
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
    $2 = this.random_helper1();
    $3 = seed;
    $1 = $2 + $3;
    $5 = this.random_helper2();
    $6 = seed;
    $4 = $5 - $6;
    $0 = $1 * $4;
    $7 = seed;
    seed = $0 + $7;
    $9 = seed;
    $8 = $9 + offset;
    return $8;
  }
  public int random_helper1() {
    int $0;
    int $2;
    int $3;
    $0 = seed;
    seed = $0 * 93185619;
    $3 = seed;
    $2 = $3 - 19386715;
    return $2;
  }
  public int random_helper2() {
    int $0;
    int $2;
    int $3;
    $0 = seed;
    seed = $0 + 73497165;
    $3 = seed;
    $2 = $3 * 45138516;
    return $2;
  }
}
