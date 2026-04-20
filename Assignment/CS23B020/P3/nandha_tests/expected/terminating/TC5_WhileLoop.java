class TC5 {
  public static void main(String[] a) {
    TC5Helper h;
    int r;
    h = new TC5Helper();
    r = h.countdown();
    System.out.println(r);
  }
}
class TC5Helper {
  public int countdown() {
    int x;
    int result;
    int t1;
    int t4;
    boolean cond;
    x = 5;
    cond = true;
    while (cond) {
      t4 = x - 1;
      x = t4;
      cond = 0 < x;
    }
    t1 = x + 1;
    result = t1;
    return result;
  }
}
