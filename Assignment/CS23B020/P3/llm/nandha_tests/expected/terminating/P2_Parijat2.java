class References {
  public static void main(String[] a) {
    Wrapper $1;
    $1 = new Wrapper();
    $1.execute();
    System.out.println(0);
  }
}
class Wrapper {
  public int execute() {
    A a;
    B b;
    int $2;
    int $3;
    b = new B();
    a = b;
    a.init_a(5);
    b.init_b(10);
    $2 = a.get_a();
    System.out.println($2);
    $3 = b.get_b();
    System.out.println($3);
    return 0;
  }
}
class A {
  int x;
  public int init_a(int k) {
    int $0;
    x = 5;
    $0 = x;
    return $0;
  }
  public int get_a() {
    int $0;
    $0 = x;
    return $0;
  }
}
class B extends A {
  int x;
  public int init_b(int k) {
    int $0;
    x = 10;
    $0 = x;
    return $0;
  }
  public int get_b() {
    int $0;
    int $1;
    int $2;
    $1 = x;
    $2 = this.get_a();
    $0 = $1 + $2;
    return $0;
  }
}
