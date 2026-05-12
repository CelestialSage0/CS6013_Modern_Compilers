class T2 {
    public static void main (String[] args) {
        T2A a1;
        T2A a2;
        T2B b1;
        int r1;
        int r2;
        int c;
        int d;
        boolean bl;
        T2A _il0_this;
        T2B _il0_b;
        int _il0_r;
        int _il0_i;
        T2A _il1_this;
        T2B _il1_b;
        int _il1_r;
        int _il1_i;
        c = 3;
        d = 4;
        a1 = new T2A();
        a2 = new T2A();
        bl = c < d;
        if (bl)
            b1 = new T2B();
        else
            b1 = new T2C();
        {
            _il0_this = a1;
            _il0_b = b1;
            _il0_i = 0;
            _il0_r = _il0_b.bar();
            System.out.println (_il0_r);
            r1 = _il0_i;
        }
        {
            _il1_this = a2;
            _il1_b = b1;
            _il1_i = 0;
            _il1_r = _il1_b.bar();
            System.out.println (_il1_r);
            r2 = _il1_i;
        }
        System.out.println (r1);
        System.out.println (r2);
    }
}
class T2A {
    public int foo(T2B b) {
        int r;
        int i;
        i = 0;
        r = b.bar();
        System.out.println (r);
        return i;
    }
}
class T2B {
    public int bar() {
        int j;
        j = 88;
        return j;
    }
}
class T2C extends T2B {
    public int bar() {
        int k;
        k = 77;
        System.out.println (k);
        return k;
    }
}
