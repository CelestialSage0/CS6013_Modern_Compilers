class Edge_NestedWhile {
  public static void main(String[] args) {
    int i;
    int j;
    int sum;
    boolean ib;
    boolean jb;
    sum = 0;
    i = 0;
    ib = true;
    while (ib) {
      j = 0;
      jb = true;
      while (jb) {
        sum = sum + 1;
        j = j + 1;
        jb = j < 3;
      }
      i = i + 1;
      ib = i < 3;
    }
    System.out.println(sum);
  }
}
