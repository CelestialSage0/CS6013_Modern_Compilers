class Main {
  public static void main(String[] p) {
    c x;
    int y;
    boolean z;
    int t;
    x = new c();
    z = true; // false
    x.b = z;
    z = x.b;
    y = x.f();
    System.out.println(y);
  }
}

class c {
  boolean b;

  public int f() {
    int x;
    if (b)
      x = this.g();
    else
      x = 3;
    return x;
  }

  public int g() {
    int x;
    if (b)
      x = this.f();
    else
      x = 3;
    return x;
  }
}

/*
Output:

class Main {
    public static void main(String[] p) {
        c x;
        int y;
        boolean z;
        int t;
        x = new c();
        x.b = true;
        System.out.println(3);
    }
}
class c {
    boolean b;

    public int f() {
      int x;
      if (b) x = this.g();
      else x = 3;
      return 3;
    }

    public int g() {
      int x;
      if (b) x = this.f();
      else x = 3;
      return 3;
    }
}
 */