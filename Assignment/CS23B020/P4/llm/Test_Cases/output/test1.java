class Main {
  public static void main(String[] args) {
    A a;
    int TEMP_0;
    a = new A();
    System.out.println(a.x);
    TEMP_0 = a.x;
  }
}

class A {
  int x;

  public int printVal() {
    System.out.println(this.x);
    return this.x;
  }
}
