class Z6 {
  public static void main(String[] args) {
    int x;
    int y;
    Foo foo;
    x = 2;
    foo = new Bar();
    y = foo.foo(x);
    System.out.println(y);
  }
}

class Foo {
  public int foo(int x) {
    x = 1;
    return x;
  }
}

class Bar extends Foo {
  public int foo(int x) {
    x = 1;
    return x;
  }
}
