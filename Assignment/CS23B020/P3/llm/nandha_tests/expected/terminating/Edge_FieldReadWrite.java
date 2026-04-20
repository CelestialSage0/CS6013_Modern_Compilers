class Edge_FieldReadWrite {
  public static void main(String[] args) {
    int r;
    Box b;
    b = new Box();
    b.x = 5;
    r = b.x;
    System.out.println(r);
  }
}
class Box {
  int x;
}
