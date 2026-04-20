class input1 {
  public static void main(String[] a) {
    Foo f;
    f = new Foo();
    f.Bar();
    System.out.println(3);
  }
}
class Foo {
  public int Bar() {
    this.Bar2(5, 20);
    return 3;
  }
  public int Bar2(int x, int y) {
    System.out.println(20);
    return 1;
  }
}
