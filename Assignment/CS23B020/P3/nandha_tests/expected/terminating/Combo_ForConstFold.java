class Combo_ForConstFold {
  public static void main(String[] args) {
    int sum;
    int i;
    boolean $0;
    sum = 0;
    i = 0;
    $0 = true;
    while ($0) {
      sum = sum + 1;
      i = i + 1;
      $0 = i < 5;
    }
    System.out.println(sum);
  }
}
