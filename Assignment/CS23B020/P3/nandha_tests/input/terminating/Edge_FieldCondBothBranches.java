class Edge_FieldCondBothBranches {
  public static void main(String[] args) {
    int x;
    int one;
    int two;
    boolean cond;
    boolean tv;
    Holder h;
    h = new Holder();
    tv = true;
    h.b = tv;
    cond = h.b;
    one = 1;
    two = 2;
    if (cond) {
      System.out.println(one);
    } else {
      System.out.println(two);
    }
  }
}
class Holder {
  boolean b;
}
