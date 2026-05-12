class T3 {
    public static void main (String[] args) {
        T3A a1;
        T3A a2;
        T3B b1;
        T3B b2;
        int r1;
        int r2;
        int temp_02;
        int temp_01;
       
        int temp_12;
        int temp_11;
        int temp_20;
        int temp_30;

        a1 = new T3A();
        a2 = new T3A();
        b1 = new T3B();
        b2 = new T3C();
        
        temp_02 = 0;

        temp_20 = 88;
        System.out.println (temp_20);
        temp_01 = temp_20;

        System.out.println (temp_01);
        r1 = temp_02;

        temp_12 = 0;
        
        temp_30 = 77;
        System.out.println (temp_30);
        temp_11 = temp_30;
        
        System.out.println (temp_11);
        r2 = temp_12;
        
        System.out.println (r1);
        System.out.println (r2);
    }
}
class T3A{
    public int foo(T3B b) {
        int r;
        int i;
        i = 0;
        r = b.bar();
        System.out.println (r);
        return i;
}
}
class T3B{
    public int bar() {
        int j;
        j = 88;
        System.out.println (j);
        return j;
    }
}
class T3C extends T3B{
    public int bar() {
        int k;
        k = 77;
        System.out.println (k);
        return k;
    }
}