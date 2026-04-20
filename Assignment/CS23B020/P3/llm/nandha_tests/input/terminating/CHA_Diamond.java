class CHA_Diamond {
  public static void main(String[] args) {
    int r;
    A a;
    a = new B();
    r = a.val();
    System.out.println(r);
    a = new C();
    r = a.val();
    System.out.println(r);
  }
}
class A {
  public int val() {
    int x;
    x = 10;
    return x;
  }
}
class B extends A {
  public int val() {
    int x;
    x = 20;
    return x;
  }
}
class C extends A {
  public int val() {
    int x;
    x = 20;
    return x;
  }
}
