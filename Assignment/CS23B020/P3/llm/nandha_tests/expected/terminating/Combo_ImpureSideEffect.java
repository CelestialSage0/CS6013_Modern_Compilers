class Combo_ImpureSideEffect {
  public static void main(String[] args) {
    Logger log;
    log = new Logger();
    log.logAndReturn(10);
    log.logAndReturn(10);
    System.out.println(0);
  }
}
class Logger {
  public int logAndReturn(int v) {
    System.out.println(10);
    return 10;
  }
}
