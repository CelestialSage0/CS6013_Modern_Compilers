class Test2 {
  public static void main(String[] a) {
    int $0;
    trial $1;
    $1 = new trial();
    $0 = $1.hello(10);
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
    j = 10;
    i = 0;
    $0 = true;
    while ($0) {
      j = j + i;
      i = i + 1;
      $0 = i < 10;
    }
    return j;
  }
}
