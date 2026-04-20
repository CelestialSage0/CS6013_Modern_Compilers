class Edge_SelfSub {
  public static void main(String[] args) {
    int x;
    int y;
    int z;
    boolean b;
    x = 7;
    y = x - x;
    b = x < x;
    if (b) {
      z = 999;
    } else {
      z = y;
    }
    System.out.println(z);
  }
}
