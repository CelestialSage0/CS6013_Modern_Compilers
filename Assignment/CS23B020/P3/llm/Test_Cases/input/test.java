class Main {
    public static void main(String[] a) {
        A x;
        int result;
        x = new A();
        result = x.foo();
        System.out.println(result);
    }
}

class A {
    public int foo() {
        int x;
        x = 2;
        return x;
    }
}