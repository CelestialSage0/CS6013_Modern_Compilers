class TC4_CHA {
  public static void main(String[] a) {
    Sanctuary s;
    int t1;
    s = new Sanctuary();
    t1 = s.run();
    System.out.println(t1);
  }
}
class Sanctuary {
  public int run() {
    Animal a;
    int t0;
    int t1;
    a = new Dog();
    t0 = 3;
    t1 = a.speak(t0);
    return t1;
  }
}
class Animal {
  public int speak(int n) {
    int t0;
    t0 = 5;
    return t0;
  }
}
class Dog extends Animal {
  public int speak(int n) {
    int t0;
    t0 = 5;
    return t0;
  }
}
class Cat extends Animal {
  public int speak(int n) {
    int t0;
    t0 = 5;
    return t0;
  }
}