class Main {
  public static void main(String[] a) {
    Calc c;
    int p;
    int q;
    int x;
    int y;

    x = 3;
    y = 7;
    c = new Calc();
    p = c.addOne(x);
    q = c.addOne(y);
    System.out.println(p);
    System.out.println(q);
  }
}

class Calc {
  public int addOne(int n) {
    int r;
    int a;
    a = 1;
    r = n + a;
    return r;
  }
}
