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
    boolean $21;
    boolean $22;
    boolean $23;
    int $27;
    int $28;
    boolean $34;
    int $36;
    boolean $41;
    arr = new int[10];
    arr[0] = 3;
    arr[1] = 1;
    arr[2] = 6;
    arr[3] = 9;
    arr[4] = 2;
    arr[5] = 8;
    arr[6] = 4;
    arr[7] = 7;
    arr[8] = 5;
    arr[9] = 10;
    iters = 0;
    $22 = this.isSorted(arr);
    $21 = !$22;
    while ($21) {
      i = 0;
      $23 = true;
      while ($23) {
        $28 = i + 1;
        $27 = arr[$28];
        arr[i] = $27;
        i = i + 1;
        $23 = i < 9;
      }
      i = 0;
      $34 = true;
      while ($34) {
        $36 = arr[i];
        System.out.println($36);
        i = i + 1;
        $34 = i < 10;
      }
      System.out.println(123456789);
      iters = iters + 1;
      $41 = this.isSorted(arr);
      $21 = !$41;
    }
    return iters;
  }
  public boolean isSorted(int[] arr) {
    int i;
    boolean sorted;
    boolean $0;
    boolean $3;
    int $4;
    int $5;
    int $7;
    sorted = true;
    i = 0;
    $0 = true;
    while ($0) {
      $5 = i + 1;
      $4 = arr[$5];
      $7 = arr[i];
      $3 = $4 < $7;
      if ($3) {
        sorted = false;
      } else {
      }
      i = i + 1;
      $0 = i < 9;
    }
    return sorted;
  }
}
