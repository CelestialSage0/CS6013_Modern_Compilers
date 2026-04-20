class CHA_ThisCall {
  public static void main(String[] args) {
    int r;
    Foo f;
    f = new Foo();
    r = f.outer();
    System.out.println(r);
  }
}
class Foo {
  public int outer() {
    int x;
    x = this.inner();
    return x;
  }
  public int inner() {
    int x;
    x = 33;
    return x;
  }
}
class FooChild extends Foo {
  public int inner() {
    int x;
    x = 44;
    return x;
  }
}
