class Arrays {
  public static void main(String[] a) {
    Wrapper $1;
    $1 = new Wrapper();
    $1.call();
    System.out.println(0);
  }
}
class Wrapper {
  int[] a;
  public int call() {
    int[] b;
    int i;
    int j;
    boolean $2;
    int $8;
    int $9;
    int $11;
    boolean $19;
    int $20;
    int $21;
    int $22;
    int[] $23;
    boolean $26;
    boolean $27;
    boolean $34;
    int $40;
    int $42;
    int $43;
    int $44;
    int[] $45;
    boolean $48;
    int $49;
    int $50;
    int $51;
    int[] $52;
    int $55;
    int[] $56;
    int $58;
    int $59;
    int $60;
    int[] $61;
    boolean $64;
    int $70;
    a = new int[10];
    b = new int[5];
    i = 0;
    $2 = true;
    while ($2) {
      $9 = i + 1;
      $11 = i + 1;
      $8 = $9 * $11;
      b[i] = $8;
      i = i + 1;
      $2 = i < 5;
    }
    i = 0;
    $23 = a;
    $22 = $23.length;
    $21 = $22 - 1;
    $20 = $21 + 1;
    $19 = 0 < $20;
    while ($19) {
      j = i;
      $27 = j < 5;
      $26 = !$27;
      while ($26) {
        j = j - 5;
        $34 = j < 5;
        $26 = !$34;
      }
      $40 = b[j];
      a[i] = $40;
      i = i + 1;
      $45 = a;
      $44 = $45.length;
      $43 = $44 - 1;
      $42 = $43 + 1;
      $19 = i < $42;
    }
    this.modify();
    i = 0;
    $52 = a;
    $51 = $52.length;
    $50 = $51 - 1;
    $49 = $50 + 1;
    $48 = 0 < $49;
    while ($48) {
      $56 = a;
      $55 = $56[i];
      System.out.println($55);
      i = i + 1;
      $61 = a;
      $60 = $61.length;
      $59 = $60 - 1;
      $58 = $59 + 1;
      $48 = i < $58;
    }
    j = 0;
    $64 = true;
    while ($64) {
      $70 = b[j];
      System.out.println($70);
      j = j + 1;
      $64 = j < 5;
    }
    return 0;
  }
  public int modify() {
    int i;
    boolean $0;
    int $1;
    int $2;
    int $3;
    int[] $4;
    int $7;
    int $8;
    int[] $9;
    int $10;
    int[] $11;
    int $13;
    int $14;
    int $15;
    int[] $16;
    i = 0;
    $4 = a;
    $3 = $4.length;
    $2 = $3 - 1;
    $1 = $2 + 1;
    $0 = 0 < $1;
    while ($0) {
      $9 = a;
      $8 = $9[i];
      $11 = a;
      $10 = $11[i];
      $7 = $8 * $10;
      a[i] = $7;
      i = i + 1;
      $16 = a;
      $15 = $16.length;
      $14 = $15 - 1;
      $13 = $14 + 1;
      $0 = i < $13;
    }
    return 0;
  }
}
