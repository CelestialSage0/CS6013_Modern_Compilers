class Test3 {
    public static void main(String[] a) {
        int TEMP_2;
        int TEMP_1;
        ArrayOps TEMP_0;
        TEMP_0 = new ArrayOps();
        TEMP_1 = TEMP_0.Run();
        TEMP_2 = TEMP_1;
        System.out.println(TEMP_2);
    }
}

class ArrayOps {
    int[] data;

    public int Run() {
        int TEMP_10;
        int TEMP_9;
        int TEMP_8;
        int TEMP_7;
        int TEMP_6;
        int TEMP_5;
        int[] TEMP_4;
        int TEMP_3;
        int sz;
        int idx;
        int val;
        TEMP_3 = 5;
        sz = TEMP_3;
        TEMP_4 = new int[sz];
        data = TEMP_4;
        TEMP_5 = 2;
        idx = TEMP_5;
        TEMP_6 = 42;
        TEMP_7 = idx;
        TEMP_8 = TEMP_6;
        data[TEMP_7] = TEMP_8;
        TEMP_9 = data[idx];
        val = TEMP_9;
        TEMP_10 = val;
        return TEMP_10;
    }

}
