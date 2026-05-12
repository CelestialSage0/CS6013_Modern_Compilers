class Main {
  public static void main(String[] args) {
    Box b;
    int w;
    b = new Box();
    /* INLINE */ w = b.getWidth();
    System.out.println(w);
  }
}

class Box {
  int width;
  int height;

  public int getWidth() {
    int tmp;
    tmp = this.width;
    return tmp;
  }
}
