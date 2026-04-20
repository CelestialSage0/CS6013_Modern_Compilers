class Main {
  public static void main(String[] p) {
    c x;
    x = new c();
    x.f();
    System.out.println(3);
  }
}
class c {
  boolean b;
  public int f() {
    this.g();
    return 3;
  }
  public int g() {
    this.f();
    return 3;
  }
}
