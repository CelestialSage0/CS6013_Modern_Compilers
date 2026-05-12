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
        T2C _il1_this;
        int _il1_k;
        T2A _il2_this;
        T2B _il2_b;
        int _il2_r;
        int _il2_i;
        T2C _il3_this;
        int _il3_k;
        c = 3;
        d = 4;
        a1 = new T2A();
        a2 = new T2A();
        bl = c < d;
        if (bl)
            b1 = new T2B();
        else
            b1 = new T2C();
 
        _il0_this = a1;
        _il0_b = b1;
        _il0_i = 0;
 
        _il1_this = _il0_b;
        _il1_k = 77;
        System.out.println (_il1_k);
        _il0_r = _il1_k;
 
        System.out.println (_il0_r);
        r1 = _il0_i;
 
 
        _il2_this = a2;
        _il2_b = b1;
        _il2_i = 0;
 
        _il3_this = _il2_b;
        _il3_k = 77;
        System.out.println (_il3_k);
        _il2_r = _il3_k;
 
        System.out.println (_il2_r);
        r2 = _il2_i;
 
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
