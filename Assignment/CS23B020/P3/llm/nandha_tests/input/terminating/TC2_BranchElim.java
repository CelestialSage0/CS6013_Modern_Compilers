// TC2: Conditional branch elimination
// if(true) -> only then branch
// if(false) -> only else branch
class TC2 {
  public static void main(String[] a) {
    TC2Helper h;
    int r1;
    int r2;
    int r3;
    h = new TC2Helper();
    r1 = h.ifTrue();
    r2 = h.ifFalse();
    r3 = h.nestedConst();
    System.out.println(r1);
    System.out.println(r2);
    System.out.println(r3);
  }
}

class TC2Helper {
  public int ifTrue() {
    boolean cond;
    boolean t1;
    int t2;
    int result;
    t1 = true;
    cond = t1;
    if (cond) {
      t2 = 42;
      result = t2;
    } else {
      t2 = 99;
      result = t2;
    }
    return result;
  }

  public int ifFalse() {
    boolean cond;
    boolean t1;
    int t2;
    int result;
    t1 = false;
    cond = t1;
    if (cond) {
      t2 = 99;
      result = t2;
    } else {
      t2 = 7;
      result = t2;
    }
    return result;
  }

  public int nestedConst() {
    int x;
    int y;
    int t1;
    int t2;
    int t3;
    int t4;
    int result;
    t1 = 10;
    x = t1;
    t2 = 20;
    y = t2;
    t3 = x + y;
    result = t3;
    t4 = 5;
    x = t4;
    t3 = x + y;
    result = t3;
    return result;
  }
}
