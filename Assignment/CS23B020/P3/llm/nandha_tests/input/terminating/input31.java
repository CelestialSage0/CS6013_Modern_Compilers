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
    int temp2;
    int a;
    int b;
    int d;
    int temp1;
    a = 5;
    d = 4;
    b = a * d;
    temp1 = this.Bar2(a, b);
    temp2 = this.Bar2(b, a);
    var1 = temp1 < temp2;
    if (var1) {
      b = 3;
    } else {
      b = 5;
    }
    return b;
  }
  public int Bar2(int x, int y) {
    int a;
    int c;
    boolean var1;
    c = 1;
    var1 = x < y;
    if (var1) {
      a = 20;
    } else {
      a = 50;
    }
    System.out.println(a);
    return c;
  }
}