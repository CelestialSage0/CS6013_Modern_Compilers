class Edge_ArrayLenLoop {
  public static void main(String[] args) {
    int[] a;
    int n;
    int i;
    int sum;
    int one;
    int sz;
    boolean b;
    sz = 4;
    one = 1;
    a = new int[sz];
    n = a.length;
    sum = 0;
    i = 0;
    b = i < n;
    while (b) {
      a[i] = i;
      sum = sum + one;
      i = i + one;
      b = i < n;
    }
    System.out.println(sum);
  }
}
