class Dead_UnreachableChild {
  public static void main(String[] args) {
    int x;
    boolean b;
    Base obj;
    b = true;
    if (b) {
      x = 1;
    } else {
      obj = new Child();
      x = obj.work();
    }
    System.out.println(x);
  }
}
class Base {
  public int work() {
    int r;
    r = 100;
    System.out.println(r);
    return r;
  }
}
class Child extends Base {
  public int work() {
    int r;
    r = 200;
    System.out.println(r);
    return r;
  }
}
