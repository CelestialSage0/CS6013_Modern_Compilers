// TC5: While loop - x starts const, becomes BOT after back-edge
// x=5 before loop, x=x-1 in loop -> x becomes BOT at header
// k=1 (never reassigned) -> should be substituted everywhere
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
    int k;
    int z;
    int result;
    int t1;
    int t2;
    int t3;
    int t4;
    boolean cond;
    t1 = 5;
    x = t1;
    t2 = 1;
    k = t2;
    t3 = 0;
    z = t3;
    cond = z < x;
    while (cond) {
      t4 = x - k;
      x = t4;
      cond = z < x;
    }
    t1 = x + k;
    result = t1;
    return result;
  }
}