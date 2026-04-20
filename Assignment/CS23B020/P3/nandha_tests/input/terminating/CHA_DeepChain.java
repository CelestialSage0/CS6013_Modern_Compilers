class CHA_DeepChain {
  public static void main(String[] args) {
    int r;
    A a;
    a = new D();
    r = a.val();
    System.out.println(r);
  }
}
class A {
  public int val() {
    int x;
    x = 42;
    return x;
  }
}
class B extends A {}
class C extends B {}
class D extends C {}
