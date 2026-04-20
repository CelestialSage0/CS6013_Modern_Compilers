class CP_ZeroMul {
  public static void main(String[] args) {
    int x;
    int y;
    int z;
    int w;
    int zero;
    int[] a;
    zero = 0;
    x = 7;
    a = new int[x];
    y = a.length;
    z = y * zero;
    w = zero * y;
    System.out.println(z);
    System.out.println(w);
  }
}
