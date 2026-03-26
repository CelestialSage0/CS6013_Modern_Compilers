class Test4 {
    public static void main(String[] a) {
        System.out.println(new Loops().Run(10));
    }
}

class Loops {
    public int Run(int n) {
        int sum;
        int i;
        sum = 0;
        i = 0;
        while (i < n) {
            sum = sum + i;
            i = i + 1;
        }
        return sum;
    }
}