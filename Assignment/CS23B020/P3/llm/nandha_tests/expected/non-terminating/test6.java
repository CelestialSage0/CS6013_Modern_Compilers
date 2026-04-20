class Main {
  public static void main(String[] p) {
    c x;
    x = new c();
    x.b = true;
    x.f();
    System.out.println(3);
  }
}
class c {
  boolean b;
  public int f() {
    if (b) {
      this.g();
    } else {
    }
    return 3;
  }
  public int g() {
    if (b) {
      this.f();
    } else {
    }
    return 3;
  }
}
