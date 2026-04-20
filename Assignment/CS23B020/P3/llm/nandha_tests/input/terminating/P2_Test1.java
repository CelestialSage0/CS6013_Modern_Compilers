class Test1 {
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
  int a;
  int b;
  public int hello(int k) {
    int c;
    int d;
    int l;
    boolean $0;
    boolean $1;
    boolean $2;
    boolean $3;
    c = 10;
    d = 14;
    $2 = k < c;
    $1 = !$2;
    $3 = k < d;
    if ($1) {
      $0 = $3;
    } else {
      $0 = false;
    }
    if ($0) {
      l = 10;
    } else {
      l = 15;
    }
    return l;
  }
}
