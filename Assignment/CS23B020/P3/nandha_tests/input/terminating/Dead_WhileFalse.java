class Dead_WhileFalse {
  public static void main(String[] args) {
    int x;
    int one;
    boolean b;
    x = 0;
    one = 1;
    b = false;
    while (b) {
      x = x + one;
    }
    System.out.println(x);
  }
}
