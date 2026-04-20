class TC5_ExprFold {
  public static void main(String[] a) {
    Folder f;
    int t1;
    f = new Folder();
    t1 = f.compute();
    System.out.println(t1);
  }
}
class Folder {
  public int compute() {
    int a;
    int b;
    int c;
    int d;
    int e;
    int result;
    boolean t0;
    boolean t1;
    int t3;
    int t4;
    int t5;
    int t6;
    int t7;
    int t8;
    int t9;
    int t10;
    int t11;
    int t12;
    t3 = 10;
    a = t3;
    t4 = 3;
    b = t4;
    t5 = a + b;
    c = t5;
    t6 = a - b;
    d = t6;
    t7 = a * b;
    e = t7;
    t0 = a < b;
    t1 = !t0;
    t8 = c + d;
    t9 = t8 + e;
    if (t1) {
      t10 = t9 + a;
      result = t10;
    } else {
      t11 = 0;
      result = t11;
    }
    t12 = result + b;
    return t12;
  }
}