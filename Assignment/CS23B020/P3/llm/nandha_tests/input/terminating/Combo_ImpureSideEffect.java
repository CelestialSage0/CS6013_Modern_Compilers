class Combo_ImpureSideEffect {
  public static void main(String[] args) {
    int x;
    int y;
    int z;
    int zero;
    Logger log;
    log = new Logger();
    x = 10;
    y = log.logAndReturn(x);
    z = log.logAndReturn(x);
    zero = 0;
    System.out.println(zero);
  }
}
class Logger {
  public int logAndReturn(int v) {
    System.out.println(v);
    return v;
  }
}
