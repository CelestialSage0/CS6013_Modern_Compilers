class Main {
  public static void main(String[] p) {
    c x;
    boolean z;
    x = new c();
    x.b = true;
    z = x.b;
    if (z) {
      System.out.println(3);
    } else {
      System.out.println(4);
    }
    x.f();
    System.out.println(3);
  }
}
class c {
  boolean b;
  public int f() {
    if (b) {
      System.out.println(2);
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
