class test1 {
  public static void main(String[] args) {
    int $0;
    A $1;
    int $2;
    $1 = new A();
    $2 = 10;
    $0 = $1.start($2);
    System.out.println($0);
  }
}
class A {
  public int start(int x) {
    int $0;
    System.out.println(x);
    x = 6;
    System.out.println(x);
    System.out.println(x);
    $0 = 1;
    return $0;
  }
}
