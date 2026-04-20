class t43 {
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
    int c;
    boolean b;
    b = true;
    a = 5;
    if (b) {
      a = 10;
    } else {
      c = a;
    }
    return a;
  }
}
