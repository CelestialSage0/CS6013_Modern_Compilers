class t51 {
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
    int a;
    int b;
    a = 1;
    b = a;
    return b;
  }
}
