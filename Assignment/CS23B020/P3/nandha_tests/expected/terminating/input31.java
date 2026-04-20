class input1 {
  public static void main(String[] a) {
    Foo f;
    f = new Foo();
    f.Bar();
    System.out.println(5);
  }
}
class Foo {
  public int Bar() {
    this.Bar2(5, 20);
    this.Bar2(20, 5);
    return 5;
  }
  public int Bar2(int x, int y) {
    int a;
    boolean var1;
    var1 = x < y;
    if (var1) {
      a = 20;
    } else {
      a = 50;
    }
    System.out.println(a);
    return 1;
  }
}
