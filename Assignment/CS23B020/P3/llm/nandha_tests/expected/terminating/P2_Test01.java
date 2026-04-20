class Test01 {
  public static void main(String[] args) {
    int $0;
    Driver $1;
    $1 = new Driver();
    $0 = $1.run();
    System.out.println($0);
  }
}
class Driver {
  int mutation;
  public int run() {
    boolean a;
    boolean c;
    boolean e;
    boolean g;
    int ctr;
    int i;
    int j;
    int k;
    int l;
    boolean $0;
    boolean $3;
    boolean $6;
    boolean $9;
    boolean $12;
    boolean $13;
    boolean $14;
    boolean $15;
    boolean $16;
    boolean $18;
    boolean $19;
    boolean $20;
    boolean $21;
    boolean $22;
    boolean $23;
    boolean $24;
    boolean $26;
    boolean $27;
    boolean $28;
    boolean $29;
    boolean $30;
    boolean $31;
    boolean $38;
    int $49;
    ctr = 0;
    i = 0;
    $0 = true;
    while ($0) {
      a = i < 1;
      j = 0;
      $3 = true;
      while ($3) {
        c = j < 1;
        k = 0;
        $6 = true;
        while ($6) {
          e = k < 1;
          l = 0;
          $9 = true;
          while ($9) {
            g = l < 1;
            if (a) {
              $16 = c;
            } else {
              $16 = false;
            }
            $15 = !$16;
            if ($15) {
              $14 = true;
            } else {
              $14 = false;
            }
            if (e) {
              $21 = true;
            } else {
              $21 = false;
            }
            $20 = !$21;
            if (g) {
              $23 = true;
            } else {
              $23 = false;
            }
            $22 = !$23;
            if ($20) {
              $19 = true;
            } else {
              $19 = $22;
            }
            $18 = !$19;
            if ($14) {
              $13 = true;
            } else {
              $13 = $18;
            }
            $24 = this.mutate();
            if ($13) {
              $12 = $24;
            } else {
              $12 = false;
            }
            if ($12) {
              ctr = ctr + 1;
            } else {
              if (a) {
                $30 = true;
              } else {
                $30 = false;
              }
              $29 = !$30;
              if (c) {
                $31 = true;
              } else {
                $31 = false;
              }
              if ($29) {
                $28 = $31;
              } else {
                $28 = false;
              }
              if ($28) {
                $27 = true;
              } else {
                $27 = false;
              }
              $38 = this.mutate();
              if ($27) {
                $26 = true;
              } else {
                $26 = $38;
              }
              if ($26) {
                ctr = ctr + 3;
              } else {
                ctr = ctr * 2;
              }
            }
            System.out.println(ctr);
            l = l + 1;
            $9 = l < 2;
          }
          k = k + 1;
          $6 = k < 2;
        }
        j = j + 1;
        $3 = j < 2;
      }
      i = i + 1;
      $0 = i < 2;
    }
    $49 = mutation;
    return $49;
  }
  public boolean mutate() {
    boolean ret;
    int $0;
    boolean $2;
    int $3;
    int $5;
    $0 = mutation;
    mutation = $0 + 1;
    $3 = mutation;
    $2 = $3 < 5;
    if ($2) {
      ret = false;
    } else {
      ret = true;
    }
    $5 = mutation;
    System.out.println($5);
    return ret;
  }
}
