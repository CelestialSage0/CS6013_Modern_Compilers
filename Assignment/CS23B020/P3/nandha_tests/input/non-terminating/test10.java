class Main {
  public static void main(String[] p) {
    c x;
    int y;
    boolean z;
    int t;
    x = new c();
    y = x.f();
    System.out.println(y);
  }
}

class c {
  boolean b;

  public int f() {
    int x;
    x = this.g();
    x = 3;
    return x;
  }

  public int g() {
    int x;
    x = this.f();
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
        x.f();
        System.out.println(3);
    }
}
class c {
    boolean b;
    public int f() {
        int x;
        this.g();
        return 3;
    }
    public int g() {
        int x;
        this.f();
        return 3;
    }
}
 */