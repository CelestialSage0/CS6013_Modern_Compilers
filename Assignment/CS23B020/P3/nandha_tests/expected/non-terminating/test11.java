class Main {
  public static void main(String[] args) {
    A a;
    a = new A();
    a.h();
    System.out.println(2);
  }
}
class A {
  public int h() {
    this.f();
    return 2;
  }
  public int f() {
    this.g();
    return 2;
  }
  public int g() {
    this.f();
    return 2;
  }
}
