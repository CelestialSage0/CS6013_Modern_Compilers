class Test5 {
  public static void main(String[] a) {
    trial $1;
    $1 = new trial();
    $1.hello(10);
    System.out.println(1);
  }
}
class trial {
  public int hello(int k) {
    A val;
    int p;
    int[] arr;
    boolean $3;
    int $7;
    val = new A();
    p = val.init(10);
    val.a = 10;
    arr = new int[p];
    $3 = 1 < p;
    if ($3) {
      arr[1] = 10;
      $7 = arr[1];
      System.out.println($7);
    } else {
      p = 0;
    }
    System.out.println(p);
    return 1;
  }
}
class A {
  int a;
  public int init(int k) {
    int $0;
    a = 10;
    $0 = a;
    return $0;
  }
}
