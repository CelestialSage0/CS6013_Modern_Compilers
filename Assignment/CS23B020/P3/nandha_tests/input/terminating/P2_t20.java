class t20 {
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
    boolean $0;
    $0 = true;
    if ($0) {
      a = 0;
    } else {
      a = 1;
    }
    return a;
  }
}
