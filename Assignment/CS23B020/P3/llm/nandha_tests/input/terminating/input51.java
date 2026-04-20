class input5 {
  public static void main(String[] a) {
    Animal ani;
    int r;
    ani = new Animal();
    r = ani.speak();
    System.out.println(r);
  }
}
class Animal {
  public int speak() {
    int x;
    x = 1;
    return x;
  }
}
class Dog extends Animal {
  public int speak() {
    int x;
    int y;
    y = 2;
    x = y;
    return x;
  }
}
