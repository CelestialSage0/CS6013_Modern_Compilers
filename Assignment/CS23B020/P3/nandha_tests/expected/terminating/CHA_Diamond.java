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
  public int val() { return 10; }
}
class B extends A {
  public int val() { return 20; }
}
class C extends A {
  public int val() { return 20; }
}
