class Test5 {
    public static void main(String[] a) {
        Dog _temp0;
        int _temp1;
        int _temp2;
        int _temp3;

        _temp0 = new Dog();
        _temp1 = 3;
        _temp2 = 7;
        _temp3 = _temp0.Info(_temp1, _temp2);
        System.out.println(_temp3);
    }
}

class Animal {
    int age;

    public int GetAge() {
        int _temp4;

        _temp4 = age;
        return _temp4;
    }
}

class Dog extends Animal {
    int speed;

    public int Info(int a, int b) {
        int total;
        int _temp5;

        age = a;
        speed = b;
        _temp5 = age + speed;
        total = _temp5;
        return total;
    }
}