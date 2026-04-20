class Edge_TempCollision {
  public static void main(String[] args) {
    int $0;
    int $1;
    int r;
    Foo f;
    f = new Foo();
    $0 = 10;
    $1 = 20;
    r = f.add($0, $1);
    System.out.println(r);
  }
}
class Foo {
  public int add(int a, int b) {
    int s;
    s = a + b;
    return s;
  }
}
