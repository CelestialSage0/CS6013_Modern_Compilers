class Main {
  public static void main(String[] p) {
    A ob;
    int val;
    boolean b;
    ob = new A();
    b = true;
    val = ob.f(b);
    System.out.println(val);
  }
}

class A {
  public int f(boolean x) {
    int result;
    if (x)
      result = this.g();
    else
      result = 5;
    return result;
  }

  public int g() {
    A a;
    boolean b;
    int x;
    a = this;
    b = false;
    x = a.f(b);
    return x;
  }
}

/*
Output:

class Main {
    public static void main(String[] p) {
        A ob;
        int val;
        boolean b;
        ob = new A();
        ob.f(true);
        System.out.println(5);
    }
}
class A {
    public int f(boolean x) {
        int result;
        if (x) {
            this.g();
        }
        else {
        }
        return 5;
    }
    public int g() {
        A a;
        boolean b;
        int x;
        a = this;
        a.f(false);
        return 5;
    }
}
 */