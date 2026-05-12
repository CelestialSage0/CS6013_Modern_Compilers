class Main {
  public static void main(String[] args) {
    Box b;
    int w;
    int TEMP_0;
    b = new Box();
    TEMP_0 = b.width;
    w = TEMP_0;
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
