class Test05 {
  public static void main(String[] args) {
    int $0;
    Driver $1;
    int $2;
    $1 = new Driver();
    $2 = 20;
    $0 = $1.run($2);
    System.out.println($0);
  }
}
class Driver {
  public int run(int x) {
    int output;
    boolean $0;
    int $1;
    boolean $2;
    int $3;
    int $4;
    int $5;
    int $6;
    int $7;
    int $8;
    int $9;
    $1 = 1;
    $0 = x < $1;
    if ($0) {
      output = 0;
    } else {
      $3 = 2;
      $2 = x < $3;
      if ($2) {
        output = 1;
      } else {
        $6 = 1;
        $5 = x - $6;
        $4 = this.run($5);
        $9 = 2;
        $8 = x - $9;
        $7 = this.run($8);
        output = $4 + $7;
      }
    }
    return output;
  }
}
