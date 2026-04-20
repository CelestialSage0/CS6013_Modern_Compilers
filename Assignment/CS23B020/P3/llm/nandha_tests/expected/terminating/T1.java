class T1 {
  public static void main(String[] args) {
    int i;
    boolean y;
    y = true;
    i = 4;
    while (y) {
      i = i + 1;
      y = i < 3;
    }
    System.out.println(i);
  }
}
