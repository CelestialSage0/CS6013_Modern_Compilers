class CP_ChainedCalls {
  public static void main(String[] args) {
    int r;
    A a;
    a = new A();
    r = a.first();
    System.out.println(r);
  }
}
class A {
  public int first() {
    int x;
    x = this.second();
    return x;
  }
  public int second() {
    int x;
    x = this.third();
    return x;
  }
  public int third() {
    int x;
    x = 777;
    return x;
  }
}
