class LinearSearch {
  public static void main(String[] a) {
    LS $1;
    $1 = new LS();
    $1.Start(10);
    System.out.println(55);
  }
}
class LS {
  int[] number;
  int size;
  public int Start(int sz) {
    int $1;
    int $3;
    int $5;
    int $7;
    this.Init(10);
    this.Print();
    System.out.println(9999);
    $1 = this.Search(8);
    System.out.println($1);
    $3 = this.Search(12);
    System.out.println($3);
    $5 = this.Search(17);
    System.out.println($5);
    $7 = this.Search(50);
    System.out.println($7);
    return 55;
  }
  public int Print() {
    int j;
    boolean $0;
    int $1;
    int $2;
    int[] $3;
    int $5;
    j = 1;
    $1 = size;
    $0 = 1 < $1;
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
  public int Search(int num) {
    int j;
    int ifound;
    int aux01;
    int aux02;
    boolean $0;
    int $1;
    int[] $2;
    boolean $4;
    boolean $5;
    boolean $6;
    int $8;
    j = 1;
    ifound = 0;
    $1 = size;
    $0 = 1 < $1;
    while ($0) {
      $2 = number;
      aux01 = $2[j];
      aux02 = num + 1;
      $4 = aux01 < num;
      if ($4) {
      } else {
        $6 = aux01 < aux02;
        $5 = !$6;
        if ($5) {
        } else {
          ifound = 1;
          j = size;
        }
      }
      j = j + 1;
      $8 = size;
      $0 = j < $8;
    }
    return ifound;
  }
  public int Init(int sz) {
    int j;
    int k;
    int aux01;
    int aux02;
    int $0;
    boolean $2;
    int $3;
    int $6;
    int $9;
    size = 10;
    number = new int[10];
    j = 1;
    $0 = size;
    k = $0 + 1;
    $3 = size;
    $2 = 1 < $3;
    while ($2) {
      aux01 = 2 * j;
      aux02 = k - 3;
      $6 = aux01 + aux02;
      number[j] = $6;
      j = j + 1;
      k = k - 1;
      $9 = size;
      $2 = j < $9;
    }
    return 0;
  }
}
