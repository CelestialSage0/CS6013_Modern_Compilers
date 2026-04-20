class Test3 {
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
class extension {
  int k;
  int l;
  public int test() {
    int $0;
    $0 = 10;
    return $0;
  }
}
class trial extends extension {
  int a;
  int b;
  public int hello(int k) {
    int x;
    int y;
    int $0;
    int $1;
    int $2;
    x = l;
    y = k;
    a = this.test();
    $1 = a;
    $2 = x + y;
    $0 = $1 - $2;
    return $0;
  }
}
