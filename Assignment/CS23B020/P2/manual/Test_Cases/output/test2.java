class Test2 {
    public static void main(String[] a) {
        Checker _temp0;
        int _temp1;

        _temp0 = new Checker();
        _temp1 = _temp0.Run();
        System.out.println(_temp1);
    }
}

class Checker {
    public boolean IsZero(int x) {
        boolean res;
        int ZERO;
        boolean _temp2;

        ZERO = 0;
        _temp2 = x < ZERO;
        if (_temp2) {
            res = false;
        } else {
            res = true;
        }
        return res;
    }

    public int Run() {
        int val;
        boolean flag;
        boolean _temp3;
        boolean _temp4;
        int _temp5;
        int _temp6;
        int _temp7;

        val = 5;
        _temp3 = this.IsZero(val);
        _temp4 = !_temp3;
        if (_temp4) {
            _temp5 = 1;
            System.out.println(_temp5);
        } else {
            _temp6 = 0;
            System.out.println(_temp6);
        }
        _temp7 = 0;
        return _temp7;
    }
}