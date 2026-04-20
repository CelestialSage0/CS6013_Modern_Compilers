class Combo_PureChainElim {
  public static void main(String[] args) {
    int unused1;
    int unused2;
    int used;
    A a;
    a = new A();
    unused1 = a.foo();
    unused2 = a.bar();
    used = 42;
    System.out.println(used);
  }
}
class A {
  public int foo() {
    int x;
    x = this.bar();
    return x;
  }
  public int bar() {
    int x;
    x = 123;
    return x;
  }
}
