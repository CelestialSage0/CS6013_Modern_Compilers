class Main {
  public static void main(String[] p) {
    a x;
    int y;
    boolean z;
    x = new b();
    z = false;
    x.b = z;
    y = x.f();
    System.out.println(y);
  }
}
class a {
  boolean b;
  public int f() {
    int x;
    if (b)
      x = this.g();
    else
      x = 4;
    return x;
  }
  public int g() {
    int x;
    x = 4;
    return x;
  }
}
class b extends a {
  public int g() {
    int x;
    x = 4;
    return x;
  }
}

/*
Output:

class Main {
    public static void main(String[] p) {
        a x;
        int y;
        boolean z;
        x = new b();
        x.b = false;
        System.out.println(4);
    }
}
class a {
    boolean b;
}
class b extends a {
}
 */