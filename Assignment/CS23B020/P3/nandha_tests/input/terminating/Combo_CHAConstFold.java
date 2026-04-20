class Combo_CHAConstFold {
  public static void main(String[] args) {
    int r;
    int s;
    int t;
    Base b;
    b = new Sub();
    r = b.get();
    s = r + r;
    t = s * r;
    System.out.println(t);
  }
}
class Base {
  public int get() {
    int x;
    x = 5;
    return x;
  }
}
class Sub extends Base {
  public int get() {
    int x;
    x = 5;
    return x;
  }
}
