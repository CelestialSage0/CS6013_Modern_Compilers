class Main {
  public static void main(String[] args) {
    int $0;
    For $1;
    int $2;
    $1 = new For();
    $2 = 10;
    $0 = $1.foo($2);
    System.out.println($0);
  }
}
class For {
  public int foo(int n) {
    int r;
    boolean $0;
    int $1;
    int $2;
    int $3;
    r = 1;
    $1 = 0;
    $0 = $1 < n;
    while ($0) {
      r = r * n;
      $2 = 1;
      n = n - $2;
      $3 = 0;
      $0 = $3 < n;
    }
    return r;
  }
}
