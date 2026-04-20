class BinarySearch {
  public static void main(String[] a) {
    int $0;
    BS $1;
    int $2;
    $1 = new BS();
    $2 = 20;
    $0 = $1.Start($2);
    System.out.println($0);
  }
}
class BS {
  int[] number;
  int size;
  public int Start(int sz) {
    int aux01;
    int aux02;
    boolean $0;
    int $1;
    int $2;
    int $3;
    boolean $4;
    int $5;
    int $6;
    int $7;
    boolean $8;
    int $9;
    int $10;
    int $11;
    boolean $12;
    int $13;
    int $14;
    int $15;
    boolean $16;
    int $17;
    int $18;
    int $19;
    boolean $20;
    int $21;
    int $22;
    int $23;
    boolean $24;
    int $25;
    int $26;
    int $27;
    boolean $28;
    int $29;
    int $30;
    int $31;
    int $32;
    aux01 = this.Init(sz);
    aux02 = this.Print();
    $1 = 8;
    $0 = this.Search($1);
    if ($0) {
      $2 = 1;
      System.out.println($2);
    } else {
      $3 = 0;
      System.out.println($3);
    }
    $5 = 19;
    $4 = this.Search($5);
    if ($4) {
      $6 = 1;
      System.out.println($6);
    } else {
      $7 = 0;
      System.out.println($7);
    }
    $9 = 20;
    $8 = this.Search($9);
    if ($8) {
      $10 = 1;
      System.out.println($10);
    } else {
      $11 = 0;
      System.out.println($11);
    }
    $13 = 21;
    $12 = this.Search($13);
    if ($12) {
      $14 = 1;
      System.out.println($14);
    } else {
      $15 = 0;
      System.out.println($15);
    }
    $17 = 37;
    $16 = this.Search($17);
    if ($16) {
      $18 = 1;
      System.out.println($18);
    } else {
      $19 = 0;
      System.out.println($19);
    }
    $21 = 38;
    $20 = this.Search($21);
    if ($20) {
      $22 = 1;
      System.out.println($22);
    } else {
      $23 = 0;
      System.out.println($23);
    }
    $25 = 39;
    $24 = this.Search($25);
    if ($24) {
      $26 = 1;
      System.out.println($26);
    } else {
      $27 = 0;
      System.out.println($27);
    }
    $29 = 50;
    $28 = this.Search($29);
    if ($28) {
      $30 = 1;
      System.out.println($30);
    } else {
      $31 = 0;
      System.out.println($31);
    }
    $32 = 999;
    return $32;
  }
  public boolean Search(int num) {
    boolean bs01;
    int right;
    int left;
    boolean var_cont;
    int medium;
    int aux01;
    int nt;
    int[] $0;
    int $1;
    int[] $2;
    boolean $3;
    int $4;
    int $5;
    boolean $6;
    boolean $7;
    boolean $8;
    aux01 = 0;
    bs01 = false;
    $0 = number;
    right = $0.length;
    $1 = 1;
    right = right - $1;
    left = 0;
    var_cont = true;
    while (var_cont) {
      medium = left + right;
      medium = this.Div(medium);
      $2 = number;
      aux01 = $2[medium];
      $3 = num < aux01;
      if ($3) {
        $4 = 1;
        right = medium - $4;
      } else {
        $5 = 1;
        left = medium + $5;
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
        nt = 0;
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
    int $0;
    boolean $1;
    int $2;
    int $3;
    count01 = 0;
    count02 = 0;
    $0 = 1;
    aux03 = num - $0;
    $1 = count02 < aux03;
    while ($1) {
      $2 = 1;
      count01 = count01 + $2;
      $3 = 2;
      count02 = count02 + $3;
      $1 = count02 < aux03;
    }
    return count01;
  }
  public boolean Compare(int num1, int num2) {
    boolean retval;
    int aux02;
    int $0;
    boolean $1;
    boolean $2;
    boolean $3;
    retval = false;
    $0 = 1;
    aux02 = num2 + $0;
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
    int $4;
    int $5;
    int $6;
    int $7;
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
    $6 = 99999;
    System.out.println($6);
    $7 = 0;
    return $7;
  }
  public int Init(int sz) {
    int j;
    int k;
    int aux02;
    int aux01;
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
