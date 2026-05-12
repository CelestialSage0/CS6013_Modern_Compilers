class Main {
  public static void main(String[] args) {
    Calc c;
    int result;
    int TEMP_0;
    c = new Calc();
    TEMP_0 = 3 + 4;
    result = TEMP_0;
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
