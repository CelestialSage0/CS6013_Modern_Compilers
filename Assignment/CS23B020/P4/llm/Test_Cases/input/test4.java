class Main {
  public static void main(String[] args) {
    Counter c;
    int v;
    c = new Counter();
    /* INLINE */ v = c.getValue();
    System.out.println(v);
  }
}

class Base {
  int value;

  public int getValue() {
    return this.value;
  }
}

class Counter extends Base {
  int step;

  public int getValue() {
    int tmp;
    tmp = this.value + this.step;
    return tmp;
  }
}
