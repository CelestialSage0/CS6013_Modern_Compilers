class Edge_PrintInLoop {
  public static void main(String[] args) {
    int i;
    boolean b;
    i = 0;
    b = true;
    while (b) {
      System.out.println(i);
      i = i + 1;
      b = i < 3;
    }
    System.out.println(i);
  }
}
