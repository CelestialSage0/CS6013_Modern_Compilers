class Main {
  public static void main(String[] args) {
    A a;
    a = new A();
    /* INLINE */ a.printVal();
  }
}

class A {
  int x;

  public int printVal() {
    int y;
    A cls;
    cls = this;
    y = cls.x;
    System.out.println(y);
    return y;
  }
}

class B extends A {

}
