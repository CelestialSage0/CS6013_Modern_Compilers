class Main {
  public static void main(String[] args) {
    A a;
    int x;
    a = new A();
    x = a.h();
    System.out.println(x);
  }
}

class A {
  public int h() {
    int x;
    x = this.f();
    return x;
  }

  public int f() {
    int x;
    x = this.g();
    x = 2;
    return x;
  }

  public int g() {
    int x;
    x = this.f();
    x = 2;
    return x;
  }
}

/*
Output:

class Main {
    public static void main(String[] args) {
        A a;
        int x;
        a = new A();
        a.h();
        System.out.println(2);
    }
}
class A {
    public int h() {
        boolean b;
        int x;
        this.f();
        return 2;
    }
    public int f() {
        int x;
        this.g();
        return 2;
    }
    public int g() {
        int x;
        this.f();
        return 2;
    }
}
 */