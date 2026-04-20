class Main {
  public static void main(String[] p) {
    a x;
    int y;
    x = new b();
    y = x.f();
    System.out.println(y);
  }
}
class a {
  public int f() {
    int x;
    x = this.g();
    return x;
  }
  public int g() { return 1; }
}
class b extends a {
  public int g() { return 2; }
}
