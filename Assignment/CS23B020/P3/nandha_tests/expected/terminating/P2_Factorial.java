class Factorial {
  public static void main(String[] a) {
    int $0;
    Fac $1;
    $1 = new Fac();
    $0 = $1.ComputeFac(10);
    System.out.println($0);
  }
}
class Fac {
  public int ComputeFac(int num) {
    int num_aux;
    boolean $0;
    int $2;
    int $3;
    $0 = num < 1;
    if ($0) {
      num_aux = 1;
    } else {
      $3 = num - 1;
      $2 = this.ComputeFac($3);
      num_aux = num * $2;
    }
    return num_aux;
  }
}
