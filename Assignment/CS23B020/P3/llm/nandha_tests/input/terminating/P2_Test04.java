class Test04 {
  public static void main(String[] args) {
    int $0;
    Driver $1;
    $1 = new Driver();
    $0 = $1.run();
    System.out.println($0);
  }
}
class Driver {
  public int run() {
    int iters;
    int i;
    int[] arr;
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
    int $11;
    int $12;
    int $13;
    int $14;
    int $15;
    int $16;
    int $17;
    int $18;
    int $19;
    int $20;
    boolean $21;
    boolean $22;
    boolean $23;
    int $24;
    int $25;
    int $26;
    int $27;
    int $28;
    int $29;
    int $30;
    int $31;
    int $32;
    int $33;
    boolean $34;
    int $35;
    int $36;
    int $37;
    int $38;
    int $39;
    int $40;
    boolean $41;
    $0 = 10;
    arr = new int[$0];
    $1 = 0;
    $2 = 3;
    arr[$1] = $2;
    $3 = 1;
    $4 = 1;
    arr[$3] = $4;
    $5 = 2;
    $6 = 6;
    arr[$5] = $6;
    $7 = 3;
    $8 = 9;
    arr[$7] = $8;
    $9 = 4;
    $10 = 2;
    arr[$9] = $10;
    $11 = 5;
    $12 = 8;
    arr[$11] = $12;
    $13 = 6;
    $14 = 4;
    arr[$13] = $14;
    $15 = 7;
    $16 = 7;
    arr[$15] = $16;
    $17 = 8;
    $18 = 5;
    arr[$17] = $18;
    $19 = 9;
    $20 = 10;
    arr[$19] = $20;
    iters = 0;
    $22 = this.isSorted(arr);
    $21 = !$22;
    while ($21) {
      i = 0;
      $25 = arr.length;
      $26 = 1;
      $24 = $25 - $26;
      $23 = i < $24;
      while ($23) {
        $29 = 1;
        $28 = i + $29;
        $27 = arr[$28];
        arr[i] = $27;
        $30 = 1;
        i = i + $30;
        $32 = arr.length;
        $33 = 1;
        $31 = $32 - $33;
        $23 = i < $31;
      }
      i = 0;
      $35 = arr.length;
      $34 = i < $35;
      while ($34) {
        $36 = arr[i];
        System.out.println($36);
        $37 = 1;
        i = i + $37;
        $38 = arr.length;
        $34 = i < $38;
      }
      $39 = 123456789;
      System.out.println($39);
      $40 = 1;
      iters = iters + $40;
      $41 = this.isSorted(arr);
      $21 = !$41;
    }
    return iters;
  }
  public boolean isSorted(int[] arr) {
    int n;
    int i;
    boolean sorted;
    boolean $0;
    int $1;
    int $2;
    boolean $3;
    int $4;
    int $5;
    int $6;
    int $7;
    int $8;
    int $9;
    int $10;
    n = arr.length;
    sorted = true;
    i = 0;
    $2 = 1;
    $1 = n - $2;
    $0 = i < $1;
    while ($0) {
      $6 = 1;
      $5 = i + $6;
      $4 = arr[$5];
      $7 = arr[i];
      $3 = $4 < $7;
      if ($3) {
        sorted = false;
      } else {
      }
      $8 = 1;
      i = i + $8;
      $10 = 1;
      $9 = n - $10;
      $0 = i < $9;
    }
    return sorted;
  }
}
