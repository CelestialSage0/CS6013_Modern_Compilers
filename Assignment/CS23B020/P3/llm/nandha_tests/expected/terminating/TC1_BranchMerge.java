class TC1_BranchMerge {
  public static void main(String[] a) {
    Worker w;
    int t1;
    w = new Worker();
    t1 = w.compute(254);
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
    int t3;
    Worker w;
    w = this;
    b1 = w.fn(254);
    b2 = w.fn(450);
    if (b1) {
      b = b2;
    } else {
      b = false;
    }
    if (b) {
      x = 254;
      y = 450;
    } else {
      x = 450;
      y = 254;
    }
    z = x + y;
    t3 = z;
    return t3;
  }
  public boolean fn(int x) {
    boolean t0;
    t0 = x < 10;
    return t0;
  }
}
