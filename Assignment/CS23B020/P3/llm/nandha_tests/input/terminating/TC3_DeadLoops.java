// TC3: Dead loop elimination
// while(cond) where cond=CONST(false) -> removed entirely
// for(i=init; cond; ...) where cond=CONST(false) -> removed
class TC3 {
  public static void main(String[] a) {
    TC3Helper h;
    int r1;
    int r2;
    h = new TC3Helper();
    r1 = h.deadWhile();
    r2 = h.deadFor();
    System.out.println(r1);
    System.out.println(r2);
  }
}

class TC3Helper {
  public int deadWhile() {
    int x;
    boolean cond;
    int t1;
    t1 = 100;
    x = t1;
    cond = false;
    while (cond) {
      t1 = 999;
      x = t1;
    }
    return x;
  }

  public int deadFor() {
    int i;
    int step;
    int x;
    boolean cond;
    int t1;
    int t2;
    int t3;
    t1 = 100;
    x = t1;
    t2 = 1;
    step = t2;
    cond = false;
    for (i = 0; cond; i = i + step) {
      t3 = 999;
      x = t3;
    }
    return x;
  }
}