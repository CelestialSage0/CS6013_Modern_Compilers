class Main {
  public static void main(String[] args) {
    Corner c;
    c = new Corner();
    c.rec();
    System.out.println(2);
  }
}
class Corner {
  public int rec() {
    this.rec();
    return 2;
  }
}
