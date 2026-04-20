class Main {
  public static void main(String[] args) {
    Corner c;
    int n;
    int rec;
    int sum;
    c = new Corner();
    rec = c.rec();
    System.out.println(rec);
  }
}

class Corner {
  public int rec() {
    int c;
    c = this.rec();
    c = 2;
    return c;
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
        c.rec();
        System.out.println(2);
    }
}
class Corner {
    public int rec() {
        int c;
        this.rec();
        return 2;
    }
}
 */