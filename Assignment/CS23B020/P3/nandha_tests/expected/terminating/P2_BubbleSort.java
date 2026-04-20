class BubbleSort {
  public static void main(String[] a) {
    BBS $1;
    $1 = new BBS();
    $1.Start(10);
    System.out.println(0);
  }
}
class BBS {
  int[] number;
  int size;
  public int Start(int sz) {
    this.Init(10);
    this.Print();
    System.out.println(99999);
    this.Sort();
    this.Print();
    return 0;
  }
  public int Sort() {
    int i;
    int aux04;
    int aux05;
    int aux06;
    int aux07;
    int j;
    int t;
    int $0;
    boolean $4;
    boolean $5;
    int $6;
    int[] $9;
    int[] $10;
    boolean $11;
    int[] $13;
    int $14;
    int[] $15;
    int $17;
    $0 = size;
    i = $0 - 1;
    $4 = -1 < i;
    while ($4) {
      j = 1;
      $6 = i + 1;
      $5 = 1 < $6;
      while ($5) {
        aux07 = j - 1;
        $9 = number;
        aux04 = $9[aux07];
        $10 = number;
        aux05 = $10[j];
        $11 = aux05 < aux04;
        if ($11) {
          aux06 = j - 1;
          $13 = number;
          t = $13[aux06];
          $15 = number;
          $14 = $15[j];
          number[aux06] = $14;
          number[j] = t;
        } else {
        }
        j = j + 1;
        $17 = i + 1;
        $5 = j < $17;
      }
      i = i - 1;
      $4 = -1 < i;
    }
    return 0;
  }
  public int Print() {
    int j;
    boolean $0;
    int $1;
    int $2;
    int[] $3;
    int $5;
    j = 0;
    $1 = size;
    $0 = 0 < $1;
    while ($0) {
      $3 = number;
      $2 = $3[j];
      System.out.println($2);
      j = j + 1;
      $5 = size;
      $0 = j < $5;
    }
    return 0;
  }
  public int Init(int sz) {
    size = 10;
    number = new int[10];
    number[0] = 20;
    number[1] = 7;
    number[2] = 12;
    number[3] = 18;
    number[4] = 2;
    number[5] = 11;
    number[6] = 6;
    number[7] = 9;
    number[8] = 19;
    number[9] = 5;
    return 0;
  }
}
