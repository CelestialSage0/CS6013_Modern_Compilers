class MoreThan4 {
  public static void main(String[] a) {
    MT4 $1;
    $1 = new MT4();
    $1.Start(1, 2, 3, 4, 5, 6);
    System.out.println(0);
  }
}
class MT4 {
  public int Start(int p1, int p2, int p3, int p4, int p5, int p6) {
    System.out.println(1);
    System.out.println(2);
    System.out.println(3);
    System.out.println(4);
    System.out.println(5);
    System.out.println(6);
    this.Change(6, 5, 4, 3, 2, 1);
    return 0;
  }
  public int Change(int p1, int p2, int p3, int p4, int p5, int p6) {
    System.out.println(6);
    System.out.println(5);
    System.out.println(4);
    System.out.println(3);
    System.out.println(2);
    System.out.println(1);
    return 0;
  }
}
