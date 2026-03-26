class Test3 {
    public static void main(String[] a) {
        ArrayOps _temp0;
        int _temp1;

        _temp0 = new ArrayOps();
        _temp1 = _temp0.Run();
        System.out.println(_temp1);
    }
}

class ArrayOps {
    int[] data;

    public int Run() {
        int sz;
        int idx;
        int val;
        int _temp2;

        sz = 5;
        data = new int[sz];
        idx = 2;
        _temp2 = 42;
        data[idx] = _temp2;
        val = data[idx];
        return val;
    }
}