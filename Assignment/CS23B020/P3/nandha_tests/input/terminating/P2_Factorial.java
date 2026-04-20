class Factorial {
  public static void main(String[] a) {
    int $0;
    Fac $1;
    int $2;
    $1 = new Fac();
    $2 = 10;
    $0 = $1.ComputeFac($2);
    System.out.println($0);
  }
}
class Fac {
  public int ComputeFac(int num) {
    int num_aux;
    boolean $0;
    int $1;
    int $2;
    int $3;
    int $4;
    $1 = 1;
    $0 = num < $1;
    if ($0) {
      num_aux = 1;
    } else {
      $4 = 1;
      $3 = num - $4;
      $2 = this.ComputeFac($3);
      num_aux = num * $2;
    }
    return num_aux;
  }
}
