class test1 {
  public static void main(String[] args) {
    A $1;
    $1 = new A();
    $1.start(10);
    System.out.println(1);
  }
}
class A {
  public int start(int x) {
    System.out.println(10);
    System.out.println(6);
    System.out.println(6);
    return 1;
  }
}
