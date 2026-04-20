class Main {
  public static void main(String[] p) {
    b x;
    int y;
    x = new b();
    y = x.f();
    System.out.println(y);
  }
}
class a {
  public int f() {
    int x;
    x = 0;
    return x;
  }
}
class b extends a {
  public int f() {
    int x;
    x = this.g();
    return x;
  }
  public int g() {
    int x;
    x = this.h();
    return x;
  }
  public int h() {
    int x;
    x = 2;
    return x;
  }
}

/*
Output:

class Main {
    public static void main(String[] p) {
        b x;
        int y;
        System.out.println(2);
    }
}
 */