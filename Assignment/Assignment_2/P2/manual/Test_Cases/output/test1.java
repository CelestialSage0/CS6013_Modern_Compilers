class Test1 {
    public static void main(String[] a) {
        Calc _temp0;
        int _temp1;
        int _temp2;

        _temp0 = new Calc();
        _temp1 = 5;
        _temp2 = _temp0.Compute(_temp1);
        System.out.println(_temp2);
    }
}

class Calc {
    public int Compute(int n) {
        int FACTOR;
        int result;

        FACTOR = 3;
        result = n * FACTOR;
        return result;
    }
}