class input1 {
  public static void main(String[] a) {
    Foo f;
    f = new Foo();
    f.Bar();
    System.out.println(1);
  }
}
class Foo {
  public int Bar() {
    System.out.println(5);
    return 1;
  }
}
