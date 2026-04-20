class Main {
  public static void main(String[] args) {
    int $0;
    t $1;
    $1 = new t();
    $0 = $1.foo();
    System.out.println($0);
  }
}
class t {
  int y;
  public int foo() {
    int[] x;
    int $0;
    int $2;
    int $4;
    int $6;
    int $7;
    $0 = this.intID(10);
    x = new int[$0];
    $2 = this.intID(3);
    $4 = this.intID(4);
    x[$2] = $4;
    $7 = this.intID(3);
    $6 = x[$7];
    return $6;
  }
  public int intID(int x) {
    int $0;
    int $2;
    $0 = y;
    y = $0 + 1;
    System.out.println(x);
    $2 = y;
    System.out.println($2);
    return x;
  }
}
