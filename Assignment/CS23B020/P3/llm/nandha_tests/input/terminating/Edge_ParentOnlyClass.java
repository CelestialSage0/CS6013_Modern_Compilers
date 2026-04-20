class Edge_ParentOnlyClass {
  public static void main(String[] args) {
    int r;
    Child c;
    c = new Child();
    r = c.val();
    System.out.println(r);
  }
}
class Parent {}
class Child extends Parent {
  public int val() {
    int x;
    x = 55;
    return x;
  }
}
