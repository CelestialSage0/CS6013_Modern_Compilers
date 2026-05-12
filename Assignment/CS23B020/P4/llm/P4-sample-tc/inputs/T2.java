
class T2 {
	public static void main (String [] args) {
		T2A a1;
		T2A a2;
		T2B b1;
		int r1;
		int r2;
		int c;
		int d;
		boolean bl;
		c = 3;
		d = 4;
		a1 = new T2A();
		a2 = new T2A();
		bl = c < d;
		if(bl)
			b1 = new T2B();
		else
			b1 = new T2C();
		/* INLINE */r1 = a1.foo(b1);
		/* INLINE */r2 = a2.foo(b1);
		System.out.println(r1);
		System.out.println(r2);
	}
}
class T2A{
	public int foo(T2B b){
		int r;
		int i;
		i = 0;
		/* INLINE */ r = b.bar();
		System.out.println(r);
		return i;
	}
}
class T2B{
	public int bar() {
		int j;
		j = 88;
		return j;
	}
}
class T2C extends T2B{
	public int bar() {
		int k;
		k = 77;
		System.out.println(k);
		return k;
	}
}