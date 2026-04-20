class Test5 {
  public static void main(String[] a) {
    int $0;
    trial $1;
    int $2;
    $1 = new trial();
    $2 = 10;
    $0 = $1.hello($2);
    System.out.println($0);
  }
}
class trial {
  public int hello(int k) {
    A val;
    int p;
    int[] arr;
    boolean x;
    int l;
    int $0;
    boolean $1;
    boolean $2;
    boolean $3;
    int $4;
    int $5;
    int $6;
    int $7;
    int $8;
    int $9;
    val = new A();
    p = val.init(k);
    $0 = 10;
    val.a = $0;
    arr = new int[p];
    l = arr.length;
    $1 = true;
    $2 = l < p;
    if ($1) {
      x = true;
    } else {
      x = $2;
    }
    $4 = 1;
    $3 = $4 < p;
    if ($3) {
      $5 = 1;
      $6 = 10;
      arr[$5] = $6;
      $8 = 1;
      $7 = arr[$8];
      System.out.println($7);
    } else {
      if (x) {
        p = 0;
      } else {
        p = 1;
      }
    }
    System.out.println(p);
    $9 = 1;
    return $9;
  }
}
class A {
  int a;
  public int init(int k) {
    int $0;
    a = k;
    $0 = a;
    return $0;
  }
}
