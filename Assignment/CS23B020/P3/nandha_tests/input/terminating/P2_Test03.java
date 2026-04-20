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
    boolean $0;
    boolean $1;
    boolean $2;
    int $3;
    int $4;
    int $5;
    int $6;
    int $7;
    sum = 0;
    $0 = true;
    object1 = this.check($0);
    $1 = false;
    object2 = this.check($1);
    i = 0;
    $3 = 10;
    $2 = i < $3;
    while ($2) {
      System.out.println(sum);
      $4 = object1.foo();
      sum = sum + $4;
      System.out.println(sum);
      $5 = object2.foo();
      sum = sum + $5;
      $6 = 1;
      i = i + $6;
      $7 = 10;
      $2 = i < $7;
    }
    return sum;
  }
  public A check(boolean b) {
    A object;
    int output;
    int $0;
    int $1;
    int $2;
    if (b) {
      object = new A();
    } else {
      object = new B();
    }
    output = object.init();
    $1 = object.foo();
    $2 = 1;
    $0 = $1 + $2;
    object.x = $0;
    output = object.foo();
    System.out.println(output);
    return object;
  }
}
class A {
  int x;
  public int init() {
    int $0;
    x = 2;
    $0 = 0;
    return $0;
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
    int $0;
    x = 3;
    $0 = 0;
    return $0;
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
