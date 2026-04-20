class LinearSearch {
  public static void main(String[] a) {
    int $0;
    LS $1;
    int $2;
    $1 = new LS();
    $2 = 10;
    $0 = $1.Start($2);
    System.out.println($0);
  }
}
class LS {
  int[] number;
  int size;
  public int Start(int sz) {
    int aux01;
    int aux02;
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
    aux01 = this.Init(sz);
    aux02 = this.Print();
    $0 = 9999;
    System.out.println($0);
    $2 = 8;
    $1 = this.Search($2);
    System.out.println($1);
    $4 = 12;
    $3 = this.Search($4);
    System.out.println($3);
    $6 = 17;
    $5 = this.Search($6);
    System.out.println($5);
    $8 = 50;
    $7 = this.Search($8);
    System.out.println($7);
    $9 = 55;
    return $9;
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
    j = 1;
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
  public int Search(int num) {
    int j;
    boolean ls01;
    int ifound;
    int aux01;
    int aux02;
    int nt;
    boolean $0;
    int $1;
    int[] $2;
    int $3;
    boolean $4;
    boolean $5;
    boolean $6;
    int $7;
    int $8;
    j = 1;
    ls01 = false;
    ifound = 0;
    $1 = size;
    $0 = j < $1;
    while ($0) {
      $2 = number;
      aux01 = $2[j];
      $3 = 1;
      aux02 = num + $3;
      $4 = aux01 < num;
      if ($4) {
        nt = 0;
      } else {
        $6 = aux01 < aux02;
        $5 = !$6;
        if ($5) {
          nt = 0;
        } else {
          ls01 = true;
          ifound = 1;
          j = size;
        }
      }
      $7 = 1;
      j = j + $7;
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
    int $1;
    boolean $2;
    int $3;
    int $4;
    int $5;
    int $6;
    int $7;
    int $8;
    int $9;
    int $10;
    size = sz;
    number = new int[sz];
    j = 1;
    $0 = size;
    $1 = 1;
    k = $0 + $1;
    $3 = size;
    $2 = j < $3;
    while ($2) {
      $4 = 2;
      aux01 = $4 * j;
      $5 = 3;
      aux02 = k - $5;
      $6 = aux01 + aux02;
      number[j] = $6;
      $7 = 1;
      j = j + $7;
      $8 = 1;
      k = k - $8;
      $9 = size;
      $2 = j < $9;
    }
    $10 = 0;
    return $10;
  }
}
