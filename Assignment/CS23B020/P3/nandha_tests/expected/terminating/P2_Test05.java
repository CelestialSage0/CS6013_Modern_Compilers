class Test05 {
  public static void main(String[] args) {
    int $0;
    Driver $1;
    $1 = new Driver();
    $0 = $1.run(20);
    System.out.println($0);
  }
}
class Driver {
  public int run(int x) {
    int output;
    boolean $0;
    boolean $2;
    int $4;
    int $5;
    int $7;
    int $8;
    $0 = x < 1;
    if ($0) {
      output = 0;
    } else {
      $2 = x < 2;
      if ($2) {
        output = 1;
      } else {
        $5 = x - 1;
        $4 = this.run($5);
        $8 = x - 2;
        $7 = this.run($8);
        output = $4 + $7;
      }
    }
    return output;
  }
}
