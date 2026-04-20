class T1 {
  public static void main(String[] args) {
    int i;
    int j;
    int a;
    int b;
    boolean x;
    boolean y;

    i = 0;
    a = 1;
    x = i < a;
    if (x) {
      j = 2;
    } else {
      j = 3;
    }

    b = 3;
    y = j < b;
    if (y) {
      i = 4;
    } else {
      i = 5;
    }

    while (y) {
      i = i + a;
      y = i < b;
    }

    System.out.println(i);
  }
}