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
        int temp_02;
        int temp_01;
        T2B temp_00;
        int temp_12;
        int temp_11;
        T2B temp_10;
        c = 3;
        d = 4;
        a1 = new T2A();
        a2 = new T2A();
        bl = c < d;
        if (bl)
            b1 = new T2B();
        else
            b1 = new T2C();

        temp_00 = b1;
        temp_02 = 0;
        temp_01 = temp_00.bar();
        System.out.println (temp_01);
        r1 = temp_02;

        temp_10 = b1;
        temp_12 = 0;
        temp_11 = temp_10.bar();    
        System.out.println (temp_11);
        r2 = temp_12;

        System.out.println (r1);
        System.out.println (r2);
    }
}
class T2A{
    public int foo(T2B b) {
        int r;
        int i;
        i = 0;
        r = b.bar();
        System.out.println (r);
        return i;
    }
}
class T2B{
    public int bar() {
        int j;
        j = 88;
        return j;
    }
}
class T2C extends T2B{
    public int bar() {
        int k;
        k = 77;
        System.out.println (k);
        return k;
    }
}

