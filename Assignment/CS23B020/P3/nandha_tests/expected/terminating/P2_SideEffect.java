class Main {
  public static void main(String[] args) {
    Foo $1;
    $1 = new Foo();
    $1.foo();
    System.out.println(4);
  }
}
class Foo {
  public int foo() {
    this.bar();
    this.baz();
    this.baz();
    this.bar();
    this.baz();
    this.bar();
    this.foobaz(2, 1);
    this.foobar(2, 2);
    return 4;
  }
  public int bar() {
    System.out.println(1);
    return 1;
  }
  public int baz() {
    System.out.println(2);
    return 2;
  }
  public int foobar(int x, int y) {
    System.out.println(238);
    return 4;
  }
  public int foobaz(int x, int y) {
    System.out.println(426);
    return 2;
  }
}
