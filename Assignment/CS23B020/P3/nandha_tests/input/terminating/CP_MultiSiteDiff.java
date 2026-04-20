class CP_MultiSiteDiff {
  public static void main(String[] args) {
    int r1;
    int r2;
    int a;
    int b;
    Calc c;
    c = new Calc();
    a = 5;
    b = 10;
    r1 = c.id(a);
    r2 = c.id(b);
    System.out.println(r1);
    System.out.println(r2);
  }
}
class Calc {
  public int id(int x) { return x; }
}
