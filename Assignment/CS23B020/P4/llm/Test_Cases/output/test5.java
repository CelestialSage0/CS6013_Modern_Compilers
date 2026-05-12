class Main {
  public static void main(String[] args) {
    Doubler d;
    int a;
    int b;
    int TEMP_0;
    d = new Doubler();
    TEMP_0 = 5 + 5;
    a = TEMP_0;
    b = d.twice(3);
    System.out.println(a);
    System.out.println(b);
  }
}

class Doubler {
  public int twice(int n) {
    int r;
    r = n + n;
    return r;
  }
}
