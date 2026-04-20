class Test08 {
  public static void main(String[] args) {
    A object;
    int a;
    object = new A();
    a = object.foo(5);
    System.out.println(a);
  }
}
class A {
  public int foo(int x) {
    boolean b;
    int y;
    b = true;
    y = 5;
    while (b) {
      y = y - 1;
      b = false;
    }
    return y;
  }
}
