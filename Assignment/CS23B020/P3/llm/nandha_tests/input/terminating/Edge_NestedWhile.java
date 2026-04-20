class Edge_NestedWhile {
  public static void main(String[] args) {
    int i;
    int j;
    int sum;
    int one;
    int three;
    boolean ib;
    boolean jb;
    one = 1;
    three = 3;
    sum = 0;
    i = 0;
    ib = i < three;
    while (ib) {
      j = 0;
      jb = j < three;
      while (jb) {
        sum = sum + one;
        j = j + one;
        jb = j < three;
      }
      i = i + one;
      ib = i < three;
    }
    System.out.println(sum);
  }
}
