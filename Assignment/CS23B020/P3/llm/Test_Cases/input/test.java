
// Input

class Test01 {
    public static void main(String[] args) {
        A object;
        int a;
        int b;
        int c;
        object = new A();
        a = 2;
        b = 3;
        a = object.foo(a);
        b = object.bar(b);
        c = 10;
        c = c * a;
        c = c + b;
        System.out.println(c);
    }
}

class A {
    public int foo(int x) {
        int z;
        z = x * x;
        return z;
    }

    public int bar(int y) {
        int z;
        z = y + y;
        return z;
    }
}

class B extends A {
    public int foo(int x) {
        int z;
        z = x + x;
        return z;
    }

    public int bar(int y) {
        int z;
        z = y * y;
        return z;
    }
}
