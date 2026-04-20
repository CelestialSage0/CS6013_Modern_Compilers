class Dead_InfiniteNoEffect {
  public static void main(String[] args) {
    int x;
    int y;
    int three;
    int ninetynine;
    boolean b;
    boolean c;
    x = 1;
    y = 2;
    three = 3;
    ninetynine = 99;
    b = x < y;
    c = false;
    if (b) {
      System.out.println(x);
    } else {
      while (c) {
        x = three;
      }
      System.out.println(y);
    }
    System.out.println(ninetynine);
  }
}
