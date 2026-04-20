class t24 {
  public static void main(String[] args) {
    int $0;
    A $1;
    $1 = new A();
    $0 = $1.foo();
    System.out.println($0);
  }
}
class A {
  public int foo() {
    boolean b;
    int a;
    b = true;
    a = 2;
    while (b) {
      a = 5;
      b = false;
    }
    return a;
  }
}
