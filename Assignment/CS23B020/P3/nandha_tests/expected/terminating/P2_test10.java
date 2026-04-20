class Main {
  public static void main(String[] args) {
    int $0;
    int $1;
    Caller $2;
    SuperType $3;
    int $4;
    Caller $5;
    SubType $6;
    $2 = new Caller();
    $3 = new SuperType();
    $1 = $2.call($3);
    $5 = new Caller();
    $6 = new SubType();
    $4 = $5.call($6);
    $0 = $1 + $4;
    System.out.println($0);
  }
}
class SuperType {
  public int value() { return 10; }
}
class SubType extends SuperType {
  public int value() { return 20; }
}
class Caller {
  public int call(SuperType s) {
    int $0;
    $0 = s.value();
    return $0;
  }
}
