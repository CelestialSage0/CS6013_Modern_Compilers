class TC1_BranchMerge {
  public static void main(String[] a) {
    Worker w;
    int t0;
    int t1;
    w = new Worker();
    t0 = 254;
    t1 = w.compute(t0);
    System.out.println(t1);
  }
}
class Worker {
  public int compute(int n) {
    int x;
    int y;
    int z;
    boolean b;
    boolean b1;
    boolean b2;
    int t0;
    int t1;
    int t2;
    int t3;
    Worker w;
    t0 = 254;
    x = t0;
    t1 = 450;
    y = t1;
    w = this;
    b1 = w.fn(t0);
    b2 = w.fn(t1);
    if (b1) {
      b = b2;
    } else {
      b = false;
    }
    if (b) {
      x = t0;
      y = t1;
    } else {
      x = t1;
      y = t0;
    }
    z = x + y;
    t2 = 0;
    t3 = z + t2;
    return t3;
  }

  public boolean fn(int x) {
    boolean t0;
    int g;
    g = 10;
    t0 = x < g;
    return t0;
  }
}