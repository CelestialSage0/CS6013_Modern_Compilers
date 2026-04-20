class CP_MultiSiteDiff {
  public static void main(String[] args) {
    int r1;
    int r2;
    Calc c;
    c = new Calc();
    r1 = c.id(5);
    r2 = c.id(10);
    System.out.println(r1);
    System.out.println(r2);
  }
}
class Calc {
  public int id(int x) { return x; }
}
