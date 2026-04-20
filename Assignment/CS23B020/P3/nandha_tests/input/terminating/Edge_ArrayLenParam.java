class Edge_ArrayLenParam {
  public static void main(String[] args) {
    int[] a;
    int sz;
    int r;
    Helper h;
    sz = 15;
    a = new int[sz];
    h = new Helper();
    r = h.getLen(a);
    System.out.println(r);
  }
}
class Helper {
  public int getLen(int[] arr) {
    int n;
    n = arr.length;
    return n;
  }
}
