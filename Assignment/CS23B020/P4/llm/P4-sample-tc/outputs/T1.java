class T1 {
    public static void main(String[] args) {
        T1A a;
        a = new T1A();
        a.foo();
    }
}

class T1A {
    int f;

    public int foo() {
        T1A x;
        T1B y;
        int val;
        int ret;
        int temp_02;
        int temp_01;
        T1B temp_00;

        x = this;
        val = 99;
        x.f = val;
        ret = x.f;
        System.out.println(ret);
        y = new T1B();

        temp_00 = y;
        temp_01 = 77;
        temp_00.f = temp_01;
        temp_02 = temp_00.f;
        System.out.println(temp_02);

        ret = x.f;
        System.out.println(ret);
        return ret;
    }
}

class T1B {
    int f;

    public int target1() {
        T1B x;
        int val;
        int ret;
        x = this;
        val = 77;
        x.f = val;
        ret = x.f;
        System.out.println(ret);
        return ret;
    }
}