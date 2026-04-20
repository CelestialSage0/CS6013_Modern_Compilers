class Main {
  public static void main(String[] p) {
    A ob;
    ob = new A();
    ob.f(true);
    System.out.println(5);
  }
}
class A {
  public int f(boolean x) {
    if (x) {
      this.g();
    } else {
    }
    return 5;
  }
  public int g() {
    A a;
    a = this;
    a.f(false);
    return 5;
  }
}
