class Calc {
  public int addOne(int n) {
    int r;
    r = n + 1;
    return r;
  }
}

class Main {
  public static void main(String[] a) {
    Calc c;
    int p;
    int q;
    c = new Calc();
    p = c.addOne(3);
    q = c.addOne(7);
    System.out.println(p);
    System.out.println(q);
  }
}
