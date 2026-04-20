class Edge_FieldReadWrite {
  public static void main(String[] args) {
    int r;
    int five;
    Box b;
    b = new Box();
    five = 5;
    b.x = five;
    r = b.x;
    System.out.println(r);
  }
}
class Box {
  int x;
}
