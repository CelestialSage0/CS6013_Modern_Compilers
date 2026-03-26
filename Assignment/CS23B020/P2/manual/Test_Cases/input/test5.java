class Test5 {
    public static void main(String[] a) {
        System.out.println(new Dog().Info(3, 7));
    }
}

class Animal {
    int age;

    public int GetAge() {
        return age;
    }
}

class Dog extends Animal {
    int speed;

    public int Info(int a, int b) {
        int total;
        age = a;
        speed = b;
        total = age + speed;
        return total;
    }
}