class Main {
  public static void main(String[] p) {
    a x;
    int y;
    x = new c();
    y = x.f();
    System.out.println(y);
  }
}
class a {
  public int f() {
    int x;
    x = 5;
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
    x = 5;
    return x;
  }
}
class c extends b {
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
    x = 5;
    return x;
  }
}

/*
Output:

class Main {
    public static void main(String[] p) {
        a x;
        int y;
        System.out.println(5);
    }
}
 */