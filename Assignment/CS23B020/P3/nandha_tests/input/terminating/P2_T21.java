class T121 {
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
    int[] x;
    A a;
    int $0;
    int $1;
    a = new A();
    $0 = a.bar();
    x = new int[$0];
    $1 = 1;
    return $1;
  }
  public int bar() {
    int $0;
    $0 = 5;
    return $0;
  }
}
