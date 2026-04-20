class Edge_PrintInLoop {
  public static void main(String[] args) {
    int i;
    int one;
    int three;
    boolean b;
    one = 1;
    three = 3;
    i = 0;
    b = i < three;
    while (b) {
      System.out.println(i);
      i = i + one;
      b = i < three;
    }
    System.out.println(i);
  }
}
