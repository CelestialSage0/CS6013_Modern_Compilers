class CP_BoolReturn {
  public static void main(String[] args) {
    int one;
    int zero;
    boolean r;
    Checker c;
    c = new Checker();
    one = 1;
    zero = 0;
    r = c.isPositive();
    if (r) {
      System.out.println(one);
    } else {
      System.out.println(zero);
    }
  }
}
class Checker {
  public boolean isPositive() {
    int x;
    int y;
    boolean r;
    x = 5;
    y = 0;
    r = y < x;
    return r;
  }
}
