class Combo_NestedDead {
  public static void main(String[] args) {
    int x;
    int y;
    int z;
    int one;
    int two;
    int three;
    int four;
    boolean a;
    boolean b;
    boolean c;
    x = 1;
    y = 2;
    z = 3;
    one = 1;
    two = 2;
    three = 3;
    four = 4;
    a = x < y;
    if (a) {
      b = y < z;
      if (b) {
        c = x < z;
        if (c) {
          System.out.println(one);
        } else {
          System.out.println(two);
        }
      } else {
        System.out.println(three);
      }
    } else {
      System.out.println(four);
    }
  }
}
