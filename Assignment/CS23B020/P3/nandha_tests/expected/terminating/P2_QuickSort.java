class QuickSort {
  public static void main(String[] a) {
    QS $1;
    $1 = new QS();
    $1.Start(10);
    System.out.println(0);
  }
}
class QS {
  int[] number;
  int size;
  public int Start(int sz) {
    int aux01;
    int $1;
    this.Init(10);
    this.Print();
    System.out.println(9999);
    $1 = size;
    aux01 = $1 - 1;
    this.Sort(0, aux01);
    this.Print();
    return 0;
  }
  public int Sort(int left, int right) {
    int v;
    int i;
    int j;
    int t;
    boolean cont01;
    boolean cont02;
    int aux03;
    boolean $0;
    int[] $1;
    int[] $4;
    boolean $5;
    boolean $6;
    int[] $8;
    boolean $9;
    boolean $10;
    int[] $11;
    int $12;
    int[] $13;
    boolean $14;
    int $15;
    int $17;
    int[] $18;
    int $19;
    int[] $20;
    int $21;
    int $23;
    t = 0;
    $0 = left < right;
    if ($0) {
      $1 = number;
      v = $1[right];
      i = left - 1;
      j = right;
      cont01 = true;
      while (cont01) {
        cont02 = true;
        while (cont02) {
          i = i + 1;
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
          j = j - 1;
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
        $15 = i + 1;
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
      $21 = i - 1;
      this.Sort(left, $21);
      $23 = i + 1;
      this.Sort($23, right);
    } else {
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
