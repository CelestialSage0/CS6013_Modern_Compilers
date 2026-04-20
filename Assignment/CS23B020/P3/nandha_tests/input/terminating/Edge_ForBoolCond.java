class Edge_ForBoolCond {
  public static void main(String[] args) {
    int x;
    int one;
    int ten;
    boolean running;
    one = 1;
    ten = 10;
    x = 0;
    running = true;
    for (x = 0; running; x = x + one) {
      running = x < ten;
    }
    System.out.println(x);
  }
}
