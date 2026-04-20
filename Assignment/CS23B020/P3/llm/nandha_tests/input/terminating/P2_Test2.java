class Test2 {
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
    int i;
    int j;
    boolean $0;
    int $1;
    int $2;
    int $3;
    j = k;
    i = 0;
    $1 = 10;
    $0 = i < $1;
    while ($0) {
      j = j + i;
      $2 = 1;
      i = i + $2;
      $3 = 10;
      $0 = i < $3;
    }
    return j;
  }
}
