// TC6: Dead class elimination
// Cat is never instantiated in reachable code -> should be removed
// Dog is instantiated -> kept even if some methods are dead
class TC6 {
  public static void main(String[] a) {
    Animal d;
    int r;
    int t1;
    d = new Dog();
    t1 = 5;
    r = d.speak(t1);
    System.out.println(r);
  }
}

class Animal {
  public int speak(int vol) {
    int t1;
    int result;
    t1 = 0;
    result = t1;
    return result;
  }
}

class Dog extends Animal {
  public int speak(int vol) {
    int t1;
    int result;
    t1 = vol + vol;
    result = t1;
    return result;
  }

  public int fetch(int dist) {
    int t1;
    int result;
    t1 = dist + dist;
    result = t1;
    return result;
  }
}

class Cat extends Animal {
  public int speak(int vol) {
    int t1;
    int result;
    t1 = 1;
    result = t1;
    return result;
  }
}
