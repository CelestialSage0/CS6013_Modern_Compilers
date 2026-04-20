class Edge_MultiArgConst {
  public static void main(String[] args) {
    int r;
    int a;
    int b;
    int c;
    Calc calc;
    calc = new Calc();
    a = 10;
    b = 20;
    c = 30;
    r = calc.sum3(a, b, c);
    System.out.println(r);
  }
}
class Calc {
  public int sum3(int x, int y, int z) {
    int t;
    int r;
    t = x + y;
    r = t + z;
    return r;
  }
}
