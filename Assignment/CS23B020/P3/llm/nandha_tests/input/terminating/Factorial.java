class Factorial {

  public static void main(String[] args) {
    int r;
    int i;
    int o;
    int n;
    Foo f;

    f = new Foo();
    o = 1;
    o = f.foo(o);
    n = 11;
    r = 1;
    for (i = 1; i < n; i = i + o) {
      r = r * i;
    }

    System.out.println(r);
  }
}

class Foo {

  int x;

  public int foo(int y) {
    boolean b;

    b = true;
    if (b) {
      y = this.bar(y);
    } else {
      y = this.baz(y);
    }

    return y;
  }

  public int bar(int z) { return z; }

  public int baz(int z) { return z; }
}
