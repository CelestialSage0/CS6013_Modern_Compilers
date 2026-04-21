class Main {
  public static void main(String[] a) {
    int x;
    int y;
    boolean cond;
    int z;
    int b;
    A aaa;

    aaa = new A();
    x = aaa.bar();
    b = 2;
    y = x * b;
    cond = false;
    if (cond) {
      z = 100;
    } else {
      z = y + b;
    }
    System.out.println(z);
  }
}

class A {
  public int foo() {
    int x;
    x = 2;
    return x;
  }

  public int bar() {
    A x;
    int y;
    x = new A();
    y = x.foo();
    return y;
  }
}
