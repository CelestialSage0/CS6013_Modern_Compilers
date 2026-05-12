
class T3 {
	public static void main(String[] args) {
		T3A a1;
		T3A a2;
		T3B b1;
		T3B b2;
		int r1;
		int r2;
		a1 = new T3A();
		a2 = new T3A();
		b1 = new T3B();
		b2 = new T3C();
		/* INLINE */r1 = a1.foo(b1);
		/* INLINE */r2 = a2.foo(b2);
		System.out.println(r1);
		System.out.println(r2);
	}
}

class T3A {
	public int foo(T3B b) {
		int r;
		int i;
		i = 0;
		r = b.bar();
		System.out.println(r);
		return i;
	}
}

class T3B {
	public int bar() {
		int j;
		j = 88;
		System.out.println(j);
		return j;
	}
}

class T3C extends T3B {
	public int bar() {
		int k;
		k = 77;
		System.out.println(k);
		return k;
	}
}
