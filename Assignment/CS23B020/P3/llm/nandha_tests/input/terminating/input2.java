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
    int c;
    int temp1;
    a = 5;
    c = 4;
    b = a * c;
    temp2 = a + b;
    temp1 = this.Bar2(a, b);
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
    int b;
    boolean var1;
    a = 10;
    b = 1;
    var1 = x < y;
    if (var1) {
      a = 20;
    } else {
      a = 50;
    }
    System.out.println(a);
    return b;
  }
}