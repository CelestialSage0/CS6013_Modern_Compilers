class Main {
  public static void main(String[] a) {
    int x;
    int y;
    boolean cond;
    int b;

    b = 1;
    x = 99;
    cond = false;
    while (cond) {
      x = x + b;
    }
    y = x + b;
    System.out.println(y);
  }
}
