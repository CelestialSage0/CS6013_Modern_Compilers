class Main {
  public static void main(String[] args) {
    Corner c;
    int n;
    int rec;
    int sum;
    c = new Corner();
    n = 5;
    rec = c.rec(n);
    sum = c.what();
    rec = rec + sum;
    System.out.println(rec);
  }
}

class Corner {
  public int rec(int n) {
    int zero;
    boolean base;
    int one;
    int next;
    int temp;
    int res;
    zero = 0;
    base = n < zero;
    if (base) {
      res = 42;
    } else {
      one = 1;
      next = n - one;
      temp = this.rec(next);
      res = 42;
    }
    return res;
  }

  public int add() {
    int a;
    int b;
    int sum;
    a = 100;
    b = 200;
    sum = a + b;
    return sum;
  }

  public int what() {
    Corner c;
    int d;
    d = 2;
    c = new Corner();
    return d;
  }
}

/*
Output:

class Main {
    public static void main(String[] args) {
        Corner c;
        int n;
        int rec;
        int sum;
        c = new Corner();
        c.rec(5);
        System.out.println(44);
    }
}
class Corner {
    public int rec(int n) {
        int zero;
        boolean base;
        int one;
        int next;
        int temp;
        int res;
        base = n < 0;
        if (base) {
        }
        else {
            next = n - 1;
            this.rec(next);
        }
        return 42;
    }
}
 */