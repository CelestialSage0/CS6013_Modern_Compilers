class t59 {
  public static void main(String[] args) {
    int $0;
    A $1;
    int $2;
    $1 = new A();
    $2 = 1;
    $0 = $1.foo($2);
    System.out.println($0);
  }
}
class A {
  public int foo(int p) {
    int i;
    int j;
    i = p;
    j = i;
    return j;
  }
}
