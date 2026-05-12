class Main {
  public static void main(String[] args) {
    Calc c;
    int result;
    c = new Calc();
    /* INLINE */ result = c.add(3, 4);
    System.out.println(result);
  }
}

class Calc {
  public int add(int x, int y) {
    int sum;
    sum = x + y;
    return sum;
  }
}
