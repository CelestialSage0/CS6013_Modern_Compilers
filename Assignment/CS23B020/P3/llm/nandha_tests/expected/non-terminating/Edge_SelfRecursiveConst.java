class Edge_SelfRecursiveConst {
  public static void main(String[] args) {
    int r;
    Rec obj;
    obj = new Rec();
    r = obj.go(5);
    System.out.println(r);
  }
}
class Rec {
  public int go(int x) {
    int r;
    r = this.go(5);
    return r;
  }
}
