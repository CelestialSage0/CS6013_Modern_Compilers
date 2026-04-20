class Edge_DeadAlloc {
  public static void main(String[] args) {
    int x;
    Junk j;
    j = new Junk();
    x = 42;
    System.out.println(x);
  }
}
class Junk {
  int a;
  int b;
}
