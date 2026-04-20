class CHA_IndependentHierarchies {
  public static void main(String[] args) {
    int r1;
    int r2;
    int sum;
    Cat c;
    Dog d;
    c = new Cat();
    d = new Dog();
    r1 = c.speak();
    r2 = d.speak();
    sum = r1 + r2;
    System.out.println(sum);
  }
}
class Cat {
  public int speak() {
    int x;
    x = 10;
    return x;
  }
}
class Dog {
  public int speak() {
    int x;
    x = 20;
    return x;
  }
}
