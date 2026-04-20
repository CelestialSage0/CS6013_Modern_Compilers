class Z3 {
  public static void main(String[] args) {
    int $0;
    Foo $1;
    $1 = new Foo();
    $0 = $1.foo();
    System.out.println($0);
  }
}
class Foo {
  int y;
  public int foo() {
    int i;
    boolean x;
    boolean z;

    x = this.baz();
    z = !x;

    if (z) {

    } else {
    }

    i = 1;

    return i;
  }

  public int bar(int x) { return y; }

  public boolean baz() {
    boolean y;
    y = true;
    return y;
  }
}
