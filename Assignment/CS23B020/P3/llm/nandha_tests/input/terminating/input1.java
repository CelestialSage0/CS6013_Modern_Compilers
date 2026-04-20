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
    a = 5;
    c = 4;
    d = 1;
    b = a * c;
    var1 = a < b;
    if (var1) {
      System.out.println(a);
    } else {
      System.out.println(b);
    }
    return d;
  }
}