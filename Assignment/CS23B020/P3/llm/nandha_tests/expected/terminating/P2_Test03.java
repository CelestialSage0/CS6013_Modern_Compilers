class Test03 {
  public static void main(String[] args) {
    int $0;
    Driver $1;
    $1 = new Driver();
    $0 = $1.run();
    System.out.println($0);
  }
}
class Driver {
  public int run() {
    A object1;
    A object2;
    int sum;
    int i;
    boolean $2;
    int $4;
    int $5;
    sum = 0;
    object1 = this.check(true);
    object2 = this.check(false);
    i = 0;
    $2 = true;
    while ($2) {
      System.out.println(sum);
      $4 = object1.foo();
      sum = sum + $4;
      System.out.println(sum);
      $5 = object2.foo();
      sum = sum + $5;
      i = i + 1;
      $2 = i < 10;
    }
    return sum;
  }
  public A check(boolean b) {
    A object;
    int output;
    int $0;
    int $1;
    if (b) {
      object = new A();
    } else {
      object = new B();
    }
    object.init();
    $1 = object.foo();
    $0 = $1 + 1;
    object.x = $0;
    output = object.foo();
    System.out.println(output);
    return object;
  }
}
class A {
  int x;
  public int init() {
    x = 2;
    return 0;
  }
  public int foo() {
    int $0;
    int $1;
    int $2;
    $0 = x;
    $1 = x;
    x = $0 * $1;
    $2 = x;
    return $2;
  }
}
class B extends A {
  int x;
  public int init() {
    x = 3;
    return 0;
  }
  public int foo() {
    int $0;
    int $1;
    int $2;
    $0 = x;
    $1 = x;
    x = $0 + $1;
    $2 = x;
    return $2;
  }
}
