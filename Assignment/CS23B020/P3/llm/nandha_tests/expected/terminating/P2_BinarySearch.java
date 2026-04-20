class BinarySearch {
  public static void main(String[] a) {
    BS $1;
    $1 = new BS();
    $1.Start(20);
    System.out.println(999);
  }
}
class BS {
  int[] number;
  int size;
  public int Start(int sz) {
    boolean $0;
    boolean $4;
    boolean $8;
    boolean $12;
    boolean $16;
    boolean $20;
    boolean $24;
    boolean $28;
    this.Init(20);
    this.Print();
    $0 = this.Search(8);
    if ($0) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    $4 = this.Search(19);
    if ($4) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    $8 = this.Search(20);
    if ($8) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    $12 = this.Search(21);
    if ($12) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    $16 = this.Search(37);
    if ($16) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    $20 = this.Search(38);
    if ($20) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    $24 = this.Search(39);
    if ($24) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    $28 = this.Search(50);
    if ($28) {
      System.out.println(1);
    } else {
      System.out.println(0);
    }
    return 999;
  }
  public boolean Search(int num) {
    boolean bs01;
    int right;
    int left;
    boolean var_cont;
    int medium;
    int aux01;
    int[] $0;
    int[] $2;
    boolean $3;
    boolean $6;
    boolean $7;
    boolean $8;
    aux01 = 0;
    $0 = number;
    right = $0.length;
    right = right - 1;
    left = 0;
    var_cont = true;
    while (var_cont) {
      medium = left + right;
      medium = this.Div(medium);
      $2 = number;
      aux01 = $2[medium];
      $3 = num < aux01;
      if ($3) {
        right = medium - 1;
      } else {
        left = medium + 1;
      }
      $6 = this.Compare(aux01, num);
      if ($6) {
        var_cont = false;
      } else {
        var_cont = true;
      }
      $7 = right < left;
      if ($7) {
        var_cont = false;
      } else {
      }
    }
    $8 = this.Compare(aux01, num);
    if ($8) {
      bs01 = true;
    } else {
      bs01 = false;
    }
    return bs01;
  }
  public int Div(int num) {
    int count01;
    int count02;
    int aux03;
    boolean $1;
    count01 = 0;
    count02 = 0;
    aux03 = num - 1;
    $1 = 0 < aux03;
    while ($1) {
      count01 = count01 + 1;
      count02 = count02 + 2;
      $1 = count02 < aux03;
    }
    return count01;
  }
  public boolean Compare(int num1, int num2) {
    boolean retval;
    int aux02;
    boolean $1;
    boolean $2;
    boolean $3;
    aux02 = num2 + 1;
    $1 = num1 < num2;
    if ($1) {
      retval = false;
    } else {
      $3 = num1 < aux02;
      $2 = !$3;
      if ($2) {
        retval = false;
      } else {
        retval = true;
      }
    }
    return retval;
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
    System.out.println(99999);
    return 0;
  }
  public int Init(int sz) {
    int j;
    int k;
    int aux02;
    int aux01;
    int $0;
    boolean $2;
    int $3;
    int $6;
    int $9;
    size = 20;
    number = new int[20];
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
