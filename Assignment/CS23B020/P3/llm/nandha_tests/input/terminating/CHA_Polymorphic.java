class CHA_Polymorphic {
  public static void main(String[] args) {
    int r;
    Base b;
    b = new Left();
    r = b.get();
    System.out.println(r);
  }
}
class Base {
  public int get() {
    int x;
    x = 1;
    return x;
  }
}
class Left extends Base {
  public int get() {
    int x;
    x = 2;
    return x;
  }
}
class Right extends Base {
  public int get() {
    int x;
    x = 3;
    return x;
  }
}
