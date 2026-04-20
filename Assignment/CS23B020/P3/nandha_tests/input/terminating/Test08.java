// Input

class Test08 {
  public static void main(String[] args) {
    A object;
    int a;
    object = new A();
    a = 5;
    a = object.foo(a);
    System.out.println(a);
  }
}

class A {
  public int foo(int x) {
    boolean b;
    int y;
    int one;
    b = true;
    one = 1;
    y = x;
    while (b) {
      y = y - one;
      b = false;
    }
    return y;
  }
}