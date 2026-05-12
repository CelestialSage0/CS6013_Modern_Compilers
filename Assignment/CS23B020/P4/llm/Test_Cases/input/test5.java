class Main {
  public static void main(String[] args) {
    Doubler d;
    int a;
    int b;
    d = new Doubler();
    /* INLINE */ a = d.twice(5);
    b = d.twice(3);
    System.out.println(a);
    System.out.println(b);
  }
}

class Doubler {
  public int twice(int n) {
    int r;
    r = n + n;
    return r;
  }
}
