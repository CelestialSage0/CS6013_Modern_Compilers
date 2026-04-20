class TC2_ConstantBranch {
  public static void main(String[] a) {
    Solver s;
    int t0;
    int t1;
    s = new Solver();
    t0 = 5;
    t1 = s.solve(t0);
    System.out.println(t1);
  }
}
class Solver {
  public int solve(int n) {
    int x;
    int y;
    int z;
    boolean t;
    boolean f;
    int t0;
    int t1;
    int t2;
    int t3;
    t = true;
    f = false;
    if (t) {
      t0 = 10;
      x = t0;
    } else {
      t0 = 99;
      x = t0;
    }
    if (f) {
      t1 = 99;
      y = t1;
    } else {
      t1 = 20;
      y = t1;
    }
    t2 = x + y;
    z = t2;
    t3 = z + n;
    return t3;
  }
}