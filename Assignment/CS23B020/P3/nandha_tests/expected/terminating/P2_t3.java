class Main {
  public static void main(String[] args) {
    t $1;
    $1 = new t();
    $1.foo();
    System.out.println(3);
  }
}
class t {
  int y;
  public int foo() {
    t x;
    t $0;
    t $1;
    t $2;
    x = new t();
    $2 = this.tID(x);
    $1 = $2.tID(x);
    $0 = $1.tID(x);
    $0.intID(3);
    return 3;
  }
  public int intID(int x) {
    int $0;
    int $2;
    $0 = y;
    y = $0 + 1;
    System.out.println(3);
    $2 = y;
    System.out.println($2);
    return 3;
  }
  public t tID(t x) {
    int $0;
    int $2;
    $0 = y;
    y = $0 + 1;
    $2 = y;
    System.out.println($2);
    return x;
  }
}
