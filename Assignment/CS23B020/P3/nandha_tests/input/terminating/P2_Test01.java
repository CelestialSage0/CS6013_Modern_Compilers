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
    boolean b;
    boolean c;
    boolean d;
    boolean e;
    boolean f;
    boolean g;
    boolean h;
    int ctr;
    int i;
    int j;
    int k;
    int l;
    boolean $0;
    int $1;
    int $2;
    boolean $3;
    int $4;
    int $5;
    boolean $6;
    int $7;
    int $8;
    boolean $9;
    int $10;
    int $11;
    boolean $12;
    boolean $13;
    boolean $14;
    boolean $15;
    boolean $16;
    boolean $17;
    boolean $18;
    boolean $19;
    boolean $20;
    boolean $21;
    boolean $22;
    boolean $23;
    boolean $24;
    int $25;
    boolean $26;
    boolean $27;
    boolean $28;
    boolean $29;
    boolean $30;
    boolean $31;
    boolean $32;
    boolean $33;
    boolean $34;
    boolean $35;
    boolean $36;
    boolean $37;
    boolean $38;
    int $39;
    int $40;
    int $41;
    int $42;
    int $43;
    int $44;
    int $45;
    int $46;
    int $47;
    int $48;
    int $49;
    b = true;
    d = false;
    if (b) {
      f = d;
    } else {
      f = false;
    }
    if (b) {
      h = true;
    } else {
      h = d;
    }
    ctr = 0;
    i = 0;
    $1 = 2;
    $0 = i < $1;
    while ($0) {
      $2 = 1;
      a = i < $2;
      j = 0;
      $4 = 2;
      $3 = j < $4;
      while ($3) {
        $5 = 1;
        c = j < $5;
        k = 0;
        $7 = 2;
        $6 = k < $7;
        while ($6) {
          $8 = 1;
          e = k < $8;
          l = 0;
          $10 = 2;
          $9 = l < $10;
          while ($9) {
            $11 = 1;
            g = l < $11;
            if (a) {
              $16 = c;
            } else {
              $16 = false;
            }
            $15 = !$16;
            if (b) {
              $17 = true;
            } else {
              $17 = d;
            }
            if ($15) {
              $14 = $17;
            } else {
              $14 = false;
            }
            if (e) {
              $21 = true;
            } else {
              $21 = f;
            }
            $20 = !$21;
            if (g) {
              $23 = h;
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
              $25 = 1;
              ctr = ctr + $25;
            } else {
              if (a) {
                $30 = b;
              } else {
                $30 = false;
              }
              $29 = !$30;
              if (c) {
                $31 = true;
              } else {
                $31 = d;
              }
              if ($29) {
                $28 = $31;
              } else {
                $28 = false;
              }
              if (e) {
                $35 = true;
              } else {
                $35 = g;
              }
              $34 = !$35;
              if (f) {
                $37 = h;
              } else {
                $37 = false;
              }
              $36 = !$37;
              if ($34) {
                $33 = true;
              } else {
                $33 = $36;
              }
              $32 = !$33;
              if ($28) {
                $27 = true;
              } else {
                $27 = $32;
              }
              $38 = this.mutate();
              if ($27) {
                $26 = true;
              } else {
                $26 = $38;
              }
              if ($26) {
                $39 = 3;
                ctr = ctr + $39;
              } else {
                $40 = 2;
                ctr = ctr * $40;
              }
            }
            System.out.println(ctr);
            $41 = 1;
            l = l + $41;
            $42 = 2;
            $9 = l < $42;
          }
          $43 = 1;
          k = k + $43;
          $44 = 2;
          $6 = k < $44;
        }
        $45 = 1;
        j = j + $45;
        $46 = 2;
        $3 = j < $46;
      }
      $47 = 1;
      i = i + $47;
      $48 = 2;
      $0 = i < $48;
    }
    $49 = mutation;
    return $49;
  }
  public boolean mutate() {
    boolean ret;
    int $0;
    int $1;
    boolean $2;
    int $3;
    int $4;
    int $5;
    $0 = mutation;
    $1 = 1;
    mutation = $0 + $1;
    $3 = mutation;
    $4 = 5;
    $2 = $3 < $4;
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
