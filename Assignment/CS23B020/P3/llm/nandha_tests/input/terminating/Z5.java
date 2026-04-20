class Z5 {
  public static void main(String[] args) {
    int x;
    int y;
    Foo foo;
    x = 1;
    foo = new Foo();
    y = foo.foo(x);
    System.out.println(y);
  }
}
class Foo {
  public int foo(int x) {
    int y;
    int z;
    boolean a;
    a = this.isEven(x);
    z = 1000;
    if (a) {
      y = x * z;
    } else {
      y = x + z;
    }

    return y;
  }

  public boolean isEven(int x) {
    int z;
    boolean a;
    z = 1;
    a = x < z;
    if (a) {
      a = true;
    } else {
      x = x - z;
      a = this.isOdd(x);
    }

    return a;
  }

  public boolean isOdd(int x) {
    int z;
    boolean a;
    z = 1;
    a = x < z;
    if (a) {
      a = false;
    } else {
      x = x - z;
      a = this.isEven(x);
    }

    return a;
  }
}
