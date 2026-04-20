class Z4 {
  public static void main(String[] args) {
    int x;
    int y;
    Foo foo;
    x = 10;
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
    y = this.bar(x);
    z = 20;
    a = y < z;
    if (a) {
      y = this.bar(y);
    } else {
      y = this.bar(z);
    }

    return y;
  }

  public int bar(int y) { return y; }
}
