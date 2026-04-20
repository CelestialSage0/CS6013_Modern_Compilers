class input1 {
  public static void main(String[] a) {
    Foo f;
    int f1;
    f = new Foo();
    f1 = f.Bar();
    System.out.println(f1);
  }
}
class Foo {
  public int Bar() {
    boolean var1;
    int a;
    int b;
    int c;
    int d;
    c = 1;
    a = 5;
    d = 4;
    b = a * d;
    var1 = a < b;
    if (var1) {
      System.out.println(a);
    } else {
      System.out.println(b);
    }
    return c;
  }
}