class Edge_SelfRecursiveConst {
  public static void main(String[] args) {
    int r;
    Rec obj;
    int five;
    obj = new Rec();
    five = 5;
    r = obj.go(five);
    System.out.println(r);
  }
}
class Rec {
  public int go(int x) {
    int one;
    int r;
    boolean b;
    one = 1;
    b = one < x;
    if (b) {
      r = this.go(x);
    } else {
      r = x;
    }
    return r;
  }
}
