class Test4 {
    public static void main(String[] a) {
        Loops _temp0;
        int _temp1;
        int _temp2;

        _temp0 = new Loops();
        _temp1 = 10;
        _temp2 = _temp0.Run(_temp1);
        System.out.println(_temp2);
    }
}

class Loops {
    public int Run(int n) {
        int sum;
        int i;
        boolean _temp3;
        int _temp4;
        int _temp5;
        int _temp6;
        boolean _temp7;

        sum = 0;
        i = 0;
        _temp3 = i < n;
        while (_temp3) {
            {
                _temp4 = sum + i;
                sum = _temp4;
                _temp5 = 1;
                _temp6 = i + _temp5;
                i = _temp6;
            }
            _temp7 = i < n;
            _temp3 = _temp7;
        }
        return sum;
    }
}