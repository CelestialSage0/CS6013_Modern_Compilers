
// input

class Test03 {
    public static void main(String[] args) {
        A object;
        int x;
        object = new A();
        /* INLINE */
        x = object.foo(5);
        System.out.println(x);
    }
}

class A {
    public int foo(int x) {
        boolean b;
        int y;
        b = x < 1;
        if (b)
            y = 1;
        else {
            x = x - 1;
            /* INLINE */
            y = this.foo(x);
            x = x + 1;
            y = y * x;
        }
        return y;
    }
}
