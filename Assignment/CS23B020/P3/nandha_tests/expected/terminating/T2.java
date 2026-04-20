class T2 {
  public static void main(String[] args) {
    int[] z;
    int a;
    boolean b;
    z = new int[10];
    z[0] = 1;
    a = z[5];
    b = a < 5;
    if (b) {
      System.out.println(5);
    } else {
      b = true;
      while (b) {
      }
    }
    System.out.println(a);
  }
}
