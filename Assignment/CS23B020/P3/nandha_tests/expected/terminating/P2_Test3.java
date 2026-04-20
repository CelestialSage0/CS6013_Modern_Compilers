class Test3 {
  public static void main(String[] a) {
    int $0;
    trial $1;
    $1 = new trial();
    $0 = $1.hello(10);
    System.out.println($0);
  }
}
class extension {
  int k;
  int l;
}
class trial extends extension {
  int a;
  int b;
  public int hello(int k) {
    int x;
    int $0;
    int $1;
    int $2;
    x = l;
    a = 10;
    $1 = a;
    $2 = x + 10;
    $0 = $1 - $2;
    return $0;
  }
}
