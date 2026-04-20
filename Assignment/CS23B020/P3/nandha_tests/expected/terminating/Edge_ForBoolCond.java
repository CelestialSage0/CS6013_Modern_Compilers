class Edge_ForBoolCond {
  public static void main(String[] args) {
    int x;
    boolean running;
    running = true;
    x = 0;
    while (running) {
      running = x < 10;
      x = x + 1;
    }
    System.out.println(x);
  }
}
