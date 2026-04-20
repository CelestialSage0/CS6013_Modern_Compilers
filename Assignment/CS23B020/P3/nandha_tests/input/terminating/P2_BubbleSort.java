class BubbleSort {
  public static void main(String[] a) {
    int $0;
    BBS $1;
    int $2;
    $1 = new BBS();
    $2 = 10;
    $0 = $1.Start($2);
    System.out.println($0);
  }
}
class BBS {
  int[] number;
  int size;
  public int Start(int sz) {
    int aux01;
    int $0;
    int $1;
    aux01 = this.Init(sz);
    aux01 = this.Print();
    $0 = 99999;
    System.out.println($0);
    aux01 = this.Sort();
    aux01 = this.Print();
    $1 = 0;
    return $1;
  }
  public int Sort() {
    int nt;
    int i;
    int aux02;
    int aux04;
    int aux05;
    int aux06;
    int aux07;
    int j;
    int t;
    int $0;
    int $1;
    int $2;
    int $3;
    boolean $4;
    boolean $5;
    int $6;
    int $7;
    int $8;
    int[] $9;
    int[] $10;
    boolean $11;
    int $12;
    int[] $13;
    int $14;
    int[] $15;
    int $16;
    int $17;
    int $18;
    int $19;
    int $20;
    $0 = size;
    $1 = 1;
    i = $0 - $1;
    $2 = 0;
    $3 = 1;
    aux02 = $2 - $3;
    $4 = aux02 < i;
    while ($4) {
      j = 1;
      $7 = 1;
      $6 = i + $7;
      $5 = j < $6;
      while ($5) {
        $8 = 1;
        aux07 = j - $8;
        $9 = number;
        aux04 = $9[aux07];
        $10 = number;
        aux05 = $10[j];
        $11 = aux05 < aux04;
        if ($11) {
          $12 = 1;
          aux06 = j - $12;
          $13 = number;
          t = $13[aux06];
          $15 = number;
          $14 = $15[j];
          number[aux06] = $14;
          number[j] = t;
        } else {
          nt = 0;
        }
        $16 = 1;
        j = j + $16;
        $18 = 1;
        $17 = i + $18;
        $5 = j < $17;
      }
      $19 = 1;
      i = i - $19;
      $4 = aux02 < i;
    }
    $20 = 0;
    return $20;
  }
  public int Print() {
    int j;
    boolean $0;
    int $1;
    int $2;
    int[] $3;
    int $4;
    int $5;
    int $6;
    j = 0;
    $1 = size;
    $0 = j < $1;
    while ($0) {
      $3 = number;
      $2 = $3[j];
      System.out.println($2);
      $4 = 1;
      j = j + $4;
      $5 = size;
      $0 = j < $5;
    }
    $6 = 0;
    return $6;
  }
  public int Init(int sz) {
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
    size = sz;
    number = new int[sz];
    $0 = 0;
    $1 = 20;
    number[$0] = $1;
    $2 = 1;
    $3 = 7;
    number[$2] = $3;
    $4 = 2;
    $5 = 12;
    number[$4] = $5;
    $6 = 3;
    $7 = 18;
    number[$6] = $7;
    $8 = 4;
    $9 = 2;
    number[$8] = $9;
    $10 = 5;
    $11 = 11;
    number[$10] = $11;
    $12 = 6;
    $13 = 6;
    number[$12] = $13;
    $14 = 7;
    $15 = 9;
    number[$14] = $15;
    $16 = 8;
    $17 = 19;
    number[$16] = $17;
    $18 = 9;
    $19 = 5;
    number[$18] = $19;
    $20 = 0;
    return $20;
  }
}
