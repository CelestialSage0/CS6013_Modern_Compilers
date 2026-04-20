class Main {
  public static void main(String[] args) {
    Corner c;
    c = new Corner();
    c.rec(5);
    System.out.println(44);
  }
}
class Corner {
  public int rec(int n) {
    boolean base;
    int next;
    base = n < 0;
    if (base) {
    } else {
      next = n - 1;
      this.rec(next);
    }
    return 42;
  }
}
