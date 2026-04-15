class A {
  public int live(int x) {
    int r;
    int k;
    k = 2;
    r = x + k;
    return r;
  }

  public int dead() {
    int z;
    z = 42;
    System.out.println(z);
    return z;
  }
}

class Main {
  public static void main(String[] a) {
    A obj;
    int result;
    obj = new A();
    result = obj.live(5);
    System.out.println(result);
  }
}
