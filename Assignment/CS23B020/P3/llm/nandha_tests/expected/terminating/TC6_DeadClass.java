class TC6 {
  public static void main(String[] a) {
    Animal d;
    int r;
    d = new Dog();
    r = d.speak(5);
    System.out.println(r);
  }
}
class Animal {
  public int speak(int vol) { return 0; }
}
class Dog extends Animal {
  public int speak(int vol) { return 10; }
}
