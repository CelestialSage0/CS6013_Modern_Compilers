// TC1: Simple constant propagation + dead assignment elimination
// x=5, y=3 -> z = x+y should become z=8, x and y assignments dead
class TC1 {
  public static void main(String[] a) {
    TC1Helper h;
    int r;
    h = new TC1Helper();
    r = h.run();
    System.out.println(r);
  }
}

class TC1Helper {
  public int run() {
    int x;
    int y;
    int z;
    int t1;
    int t2;
    int t3;
    t1 = 5;
    x = t1;
    t2 = 3;
    y = t2;
    t3 = x + y;
    z = t3;
    return z;
  }
}
