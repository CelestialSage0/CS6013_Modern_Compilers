class t52 {
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
    int[] a;
    int b;
    int $0;
    $0 = 10;
    a = new int[$0];
    b = 1;
    a[b] = b;
    return b;
  }
}
