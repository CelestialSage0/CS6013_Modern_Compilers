class Need_ImpureDowngrade {
  public static void main(String[] args) {
    int unused;
    int zero;
    Printer p;
    p = new Printer();
    unused = p.printAndReturn();
    zero = 0;
    System.out.println(zero);
  }
}
class Printer {
  public int printAndReturn() {
    int x;
    x = 42;
    System.out.println(x);
    return x;
  }
}
