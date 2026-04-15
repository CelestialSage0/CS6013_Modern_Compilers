class A {
  public int live(int x) {
    int r;
    r = x + 2;
    return r;
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
