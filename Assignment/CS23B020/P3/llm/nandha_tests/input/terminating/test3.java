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
  public int g() {
    int x;
    x = 1;
    return x;
  }
}

class b extends a {
  public int g() {
    int x;
    x = 2;
    return x;
  }
}

/*
Output:

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
    public int g() {
        int x;
        return 1;
    }
}
class b extends a {
    public int g() {
        int x;
        return 2;
    }
}
 */