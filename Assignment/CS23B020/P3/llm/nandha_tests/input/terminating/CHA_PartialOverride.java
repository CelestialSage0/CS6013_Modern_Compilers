class CHA_PartialOverride {
  public static void main(String[] args) {
    int r;
    Base b;
    b = new NoOverride();
    r = b.val();
    System.out.println(r);
  }
}
class Base {
  public int val() {
    int x;
    x = 50;
    return x;
  }
}
class Override extends Base {
  public int val() {
    int x;
    x = 60;
    return x;
  }
}
class NoOverride extends Base {}
