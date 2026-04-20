class Dead_TransitiveUnreachable {
  public static void main(String[] args) {
    int x;
    int y;
    int one;
    boolean b;
    Wrapper w;
    w = new Wrapper();
    x = 5;
    y = 10;
    one = 1;
    b = x < y;
    if (b) {
      System.out.println(one);
    } else {
      x = w.callInner();
      System.out.println(x);
    }
  }
}
class Wrapper {
  public int callInner() {
    int r;
    Inner i;
    i = new Inner();
    r = i.compute();
    return r;
  }
}
class Inner {
  public int compute() {
    int x;
    x = 999;
    return x;
  }
}
