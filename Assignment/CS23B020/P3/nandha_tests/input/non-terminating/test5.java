class Main {
  public static void main(String[] p) {
    c x;
    int y;
    boolean z;
    int t;
    x = new c();
    z = true;
    x.b = z;
    z = x.b;
    if (z) {
      t = 3;
      System.out.println(t);
    } else {
      t = 4;
      System.out.println(t);
    }
    y = x.f();
    System.out.println(y);
  }
}

class c {
  boolean b;

  public int f() {
    int x;
    if (b) {
      x = 2;
      System.out.println(x);
      x = this.g();
    } else
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
        z = x.b;
        if (z) {
            System.out.println(3);
        }
        else {
            System.out.println(4);
        }
        x.f();
        System.out.println(3);
    }
}
class c {
    boolean b;
    public int f() {
        int x;
        if (b) {
            System.out.println(2);
            this.g();
        }
        else {
        }
        return 3;
    }
    public int g() {
        int x;
        if (b) {
            this.f();
        }
        else {
        }
        return 3;
    }
}
 */