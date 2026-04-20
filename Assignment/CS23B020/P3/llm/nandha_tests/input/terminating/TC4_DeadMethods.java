// TC4: Dead method elimination
// neverCalled() should be removed from output
// pureUnused() result assigned but never used -> assignment dead
class TC4 {
  public static void main(String[] a) {
    TC4Helper h;
    int r;
    h = new TC4Helper();
    r = h.used();
    System.out.println(r);
  }
}

class TC4Helper {
  public int used() {
    int t1;
    int result;
    t1 = 42;
    result = t1;
    return result;
  }

  public int neverCalled() {
    int t1;
    int x;
    t1 = 999;
    x = t1;
    return x;
  }

  public int alsoNeverCalled() {
    int t1;
    t1 = 0;
    return t1;
  }
}
