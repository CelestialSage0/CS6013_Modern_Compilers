class QuickSort {
  public static void main(String[] a) {
    int $0;
    QS $1;
    int $2;
    $1 = new QS();
    $2 = 10;
    $0 = $1.Start($2);
    System.out.println($0);
  }
}
class QS {
  int[] number;
  int size;
  public int Start(int sz) {
    int aux01;
    int $0;
    int $1;
    int $2;
    int $3;
    int $4;
    aux01 = this.Init(sz);
    aux01 = this.Print();
    $0 = 9999;
    System.out.println($0);
    $1 = size;
    $2 = 1;
    aux01 = $1 - $2;
    $3 = 0;
    aux01 = this.Sort($3, aux01);
    aux01 = this.Print();
    $4 = 0;
    return $4;
  }
  public int Sort(int left, int right) {
    int v;
    int i;
    int j;
    int nt;
    int t;
    boolean cont01;
    boolean cont02;
    int aux03;
    boolean $0;
    int[] $1;
    int $2;
    int $3;
    int[] $4;
    boolean $5;
    boolean $6;
    int $7;
    int[] $8;
    boolean $9;
    boolean $10;
    int[] $11;
    int $12;
    int[] $13;
    boolean $14;
    int $15;
    int $16;
    int $17;
    int[] $18;
    int $19;
    int[] $20;
    int $21;
    int $22;
    int $23;
    int $24;
    int $25;
    t = 0;
    $0 = left < right;
    if ($0) {
      $1 = number;
      v = $1[right];
      $2 = 1;
      i = left - $2;
      j = right;
      cont01 = true;
      while (cont01) {
        cont02 = true;
        while (cont02) {
          $3 = 1;
          i = i + $3;
          $4 = number;
          aux03 = $4[i];
          $6 = aux03 < v;
          $5 = !$6;
          if ($5) {
            cont02 = false;
          } else {
            cont02 = true;
          }
        }
        cont02 = true;
        while (cont02) {
          $7 = 1;
          j = j - $7;
          $8 = number;
          aux03 = $8[j];
          $10 = v < aux03;
          $9 = !$10;
          if ($9) {
            cont02 = false;
          } else {
            cont02 = true;
          }
        }
        $11 = number;
        t = $11[i];
        $13 = number;
        $12 = $13[j];
        number[i] = $12;
        number[j] = t;
        $16 = 1;
        $15 = i + $16;
        $14 = j < $15;
        if ($14) {
          cont01 = false;
        } else {
          cont01 = true;
        }
      }
      $18 = number;
      $17 = $18[i];
      number[j] = $17;
      $20 = number;
      $19 = $20[right];
      number[i] = $19;
      number[right] = t;
      $22 = 1;
      $21 = i - $22;
      nt = this.Sort(left, $21);
      $24 = 1;
      $23 = i + $24;
      nt = this.Sort($23, right);
    } else {
      nt = 0;
    }
    $25 = 0;
    return $25;
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
