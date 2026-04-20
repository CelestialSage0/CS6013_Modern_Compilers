class Combo_ForConstFold {
  public static void main(String[] args) {
    int sum;
    int i;
    int one;
    int limit;
    one = 1;
    limit = 5;
    sum = 0;
    for (i = 0; i < limit; i = i + one) {
      sum = sum + one;
    }
    System.out.println(sum);
  }
}
