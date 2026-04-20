class T2 {
  public static void main(String[] args) {
    int x;
    int y;
    int[] z;
    int a;
    int c;
    boolean b;

    a = 10;
    z = new int[a];
    a = 1;
    x = 0;
    y = x;
    z[x] = a;
    x = 5;
    x = x * a;
    y = y - x;
    a = 0;
    c = z[x];
    y = c + a;

    a = z[x];
    b = a < x;
    if (b) {
      System.out.println(x);
    } else {
      x = 2;
      y = 3;
      b = x < y;
      while (b) {
        x = 2;
      }

      System.out.println(y);
    }

    System.out.println(a);
  }
}