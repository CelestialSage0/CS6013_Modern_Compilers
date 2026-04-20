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
  public int get() { return 1; }
}
class Left extends Base {
  public int get() { return 2; }
}
