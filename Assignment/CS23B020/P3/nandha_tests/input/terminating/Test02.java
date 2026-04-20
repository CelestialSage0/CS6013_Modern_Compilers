// Input

class Test02 {
  public static void main(String[] args) {
    A object;
    int a;
    int i;
    int j;
    int k;
    int l;
    object = new A();
    a = 2;
    j = 10;
    k = 1;
    for (i = j; i < j; i = i + k) {
      a = object.foo(a);
    }
    System.out.println(a);
    a = 3;
    k = 11;
    for (i = j; i < k; i = i + k) {
      a = object.bar(a);
    }
    System.out.println(a);
    a = 4;
    j = 0;
    k = 10;
    l = 1;
    for (i = j; i < k; i = i + l) {
      a = object.fubar(a, k);
    }
    System.out.println(a);
  }
}

class A {
  public int foo(int x) {
    int z;
    z = 111;
    System.out.println(z);
    z = x * x;
    return z;
  }

  public int bar(int y) {
    int z;
    z = 222;
    System.out.println(z);
    z = y + y;
    return z;
  }

  public int fubar(int x, int y) {
    int z;
    z = 333;
    System.out.println(z);
    z = x + y;
    z = z * x;
    z = z * y;
    return z;
  }
}