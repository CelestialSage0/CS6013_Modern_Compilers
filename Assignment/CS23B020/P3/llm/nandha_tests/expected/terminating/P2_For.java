class Main {
  public static void main(String[] args) {
    int $0;
    For $1;
    $1 = new For();
    $0 = $1.foo(10);
    System.out.println($0);
  }
}
class For {
  public int foo(int n) {
    int r;
    boolean $0;
    r = 1;
    $0 = true;
    while ($0) {
      r = r * n;
      n = n - 1;
      $0 = 0 < n;
    }
    return r;
  }
}
