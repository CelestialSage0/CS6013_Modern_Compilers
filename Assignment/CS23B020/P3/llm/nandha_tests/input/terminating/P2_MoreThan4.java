class MoreThan4 {
  public static void main(String[] a) {
    int $0;
    MT4 $1;
    int $2;
    int $3;
    int $4;
    int $5;
    int $6;
    int $7;
    $1 = new MT4();
    $2 = 1;
    $3 = 2;
    $4 = 3;
    $5 = 4;
    $6 = 5;
    $7 = 6;
    $0 = $1.Start($2, $3, $4, $5, $6, $7);
    System.out.println($0);
  }
}
class MT4 {
  public int Start(int p1, int p2, int p3, int p4, int p5, int p6) {
    int aux;
    System.out.println(p1);
    System.out.println(p2);
    System.out.println(p3);
    System.out.println(p4);
    System.out.println(p5);
    System.out.println(p6);
    aux = this.Change(p6, p5, p4, p3, p2, p1);
    return aux;
  }
  public int Change(int p1, int p2, int p3, int p4, int p5, int p6) {
    int $0;
    System.out.println(p1);
    System.out.println(p2);
    System.out.println(p3);
    System.out.println(p4);
    System.out.println(p5);
    System.out.println(p6);
    $0 = 0;
    return $0;
  }
}
