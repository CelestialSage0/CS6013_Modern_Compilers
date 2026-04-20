class Need_ImpureDowngrade {
  public static void main(String[] args) {
    Printer p;
    p = new Printer();
    p.printAndReturn();
    System.out.println(0);
  }
}
class Printer {
  public int printAndReturn() {
    System.out.println(42);
    return 42;
  }
}
