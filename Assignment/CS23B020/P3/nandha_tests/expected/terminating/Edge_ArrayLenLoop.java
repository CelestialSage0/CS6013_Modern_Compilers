class Edge_ArrayLenLoop {
  public static void main(String[] args) {
    int[] a;
    int i;
    int sum;
    boolean b;
    a = new int[4];
    sum = 0;
    i = 0;
    b = true;
    while (b) {
      a[i] = i;
      sum = sum + 1;
      i = i + 1;
      b = i < 4;
    }
    System.out.println(sum);
  }
}
