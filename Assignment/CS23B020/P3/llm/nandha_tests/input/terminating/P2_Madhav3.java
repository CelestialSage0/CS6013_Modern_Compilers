class Test5 {
  public static void main(String[] args) {
    int $0;
    D $1;
    $1 = new D();
    $0 = $1.change();
    System.out.println($0);
  }
}
class A {
  int x;
  public int change() {
    int $0;
    x = 1;
    $0 = x;
    return $0;
  }
}
class B extends A {
  int x;
  public int change() {
    int $0;
    x = 2;
    $0 = x;
    return $0;
  }
}
class C extends B {
  public int hey() {
    int k;
    int $0;
    k = this.change();
    $0 = x;
    return $0;
  }
}
class D extends C {
  public int change() {
    int $0;
    C $1;
    $1 = new C();
    $0 = $1.hey();
    return $0;
  }
}
