class Test2 {
    public static void main(String[] a) {
        int TEMP_1;
        Checker TEMP_0;
        TEMP_0 = new Checker();
        TEMP_1 = TEMP_0.Run();
        System.out.println(TEMP_1);
    }
}

class Checker {
    public boolean IsZero(int x) {
        boolean res;
        int ZERO = 0;
        boolean TEMP_2;
        boolean TEMP_3;
        TEMP_2 = x < ZERO;
        if (TEMP_2) {
            res = false;
        } else {
            res = true;
        }
        TEMP_3 = res;
        return TEMP_3;
    }

    public int Run() {
        int val;
        int TEMP_9;
        int TEMP_8;
        int TEMP_7;
        boolean TEMP_6;
        boolean TEMP_5;
        boolean TEMP_4;
        val = 5;
        TEMP_4 = this.IsZero(val);
        TEMP_5 = !TEMP_4;
        TEMP_6 = TEMP_5;
        if (TEMP_6) {
            TEMP_7 = 1;
            System.out.println(TEMP_7);
        } else {
            TEMP_8 = 0;
            System.out.println(TEMP_8);
        }
        TEMP_9 = 0;
        return TEMP_9;
    }

}
