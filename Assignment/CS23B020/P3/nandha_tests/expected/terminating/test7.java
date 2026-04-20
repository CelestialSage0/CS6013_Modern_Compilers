class Main {
  public static void main(String[] p) {
    c x;
    int y;
    x = new c();
    x.b = true;
    y = x.f();
    System.out.println(y);
  }
}
class c {
  boolean b;
  public int f() {
    int x;
    if (b) {
      x = this.g();
    } else {
      x = 3;
    }
    return x;
  }
  public int g() {
    int x;
    if (b) {
      x = this.f2();
    } else {
      x = 3;
    }
    return x;
  }
  public int f2() {
    int x;
    if (b) {
      x = 1;
    } else {
      x = 3;
    }
    return x;
  }
}
