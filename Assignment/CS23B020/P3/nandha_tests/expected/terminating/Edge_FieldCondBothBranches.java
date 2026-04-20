class Edge_FieldCondBothBranches {
  public static void main(String[] args) {
    boolean cond;
    Holder h;
    h = new Holder();
    h.b = true;
    cond = h.b;
    if (cond) {
      System.out.println(1);
    } else {
      System.out.println(2);
    }
  }
}
class Holder {
  boolean b;
}
