class Test09 {
  public static void main(String[] args) {
    A object;
    int a;
    int i;
    boolean b;
    object = new A();
    System.out.println(2);
    a = 3;
    i = 10;
    b = true;
    while (b) {
      a = object.bar(a);
      i = i + 11;
      b = i < 11;
    }
    System.out.println(a);
    a = 4;
    i = 0;
    b = true;
    while (b) {
      a = object.fubar(a, 10);
      i = i + 1;
      b = i < 10;
    }
    System.out.println(a);
  }
}
class A {
  public int bar(int y) {
    int z;
    System.out.println(222);
    z = y + y;
    return z;
  }
  public int fubar(int x, int y) {
    int z;
    System.out.println(333);
    z = x + 10;
    z = z * x;
    z = z * 10;
    return z;
  }
}
