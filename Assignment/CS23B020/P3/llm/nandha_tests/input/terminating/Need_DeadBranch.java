class Need_DeadBranch {
  public static void main(String[] args) {
    int x;
    int y;
    int z;
    boolean cond;
    x = 1;
    y = 2;
    cond = x < y;
    if (cond) {
      z = 10;
    } else {
      z = 20;
    }
    System.out.println(z);
  }
}
