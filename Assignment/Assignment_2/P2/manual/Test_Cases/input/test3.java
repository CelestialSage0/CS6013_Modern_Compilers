class Test3 {
    public static void main(String[] a) {
        System.out.println(new ArrayOps().Run());
    }
}

class ArrayOps {
    int[] data;

    public int Run() {
        int sz;
        int idx;
        int val;
        sz = 5;
        data = new int[sz];
        idx = 2;
        data[idx] = 42;
        val = data[idx];
        return val;
    }
}