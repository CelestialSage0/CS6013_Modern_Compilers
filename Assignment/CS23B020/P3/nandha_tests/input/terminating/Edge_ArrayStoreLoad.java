class Edge_ArrayStoreLoad {
  public static void main(String[] args) {
    int[] a;
    int sz;
    int idx;
    int val;
    int r;
    sz = 5;
    a = new int[sz];
    idx = 0;
    val = 77;
    a[idx] = val;
    r = a[idx];
    System.out.println(r);
  }
}
