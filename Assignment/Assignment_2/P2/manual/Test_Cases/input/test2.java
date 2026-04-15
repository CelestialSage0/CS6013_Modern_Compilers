class Test2 {
    public static void main(String[] a) {
        System.out.println(new Checker().Run());
    }
}

class Checker {
    public boolean IsZero(int x) {
        boolean res;
        final int ZERO = 0;
        if (x < ZERO)
            res = false;
        else
            res = true;
        return res;
    }

    public int Run() {
        int val;
        val = 5;
        if (!this.IsZero(val))
            System.out.println(1);
        else
            System.out.println(0);
        return 0;
    }
}