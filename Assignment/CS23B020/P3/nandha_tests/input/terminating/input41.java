class input4 {
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
    int a;
    int b;
    int c;
    int d;
    int e;
    int result;
    boolean cond;
    a = 3;
    b = 7;
    c = a + b;
    d = 2;
    result = c * d;
    cond = b < a;
    while (cond) {
      e = 1;
      result = result + e;
    }
    return result;
  }
}
