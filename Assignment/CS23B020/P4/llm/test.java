
// input

class Test02 {
    public static void main(String[] args) {
        FooInator fooer;
        A object;
        fooer = new FooInator();
        object = new A();
        /* INLINE */
        fooer.fooIfy(object);
        object = new B();
        /* INLINE */
        fooer.fooIfy(object);
        object = new C();
        /* INLINE */
        fooer.fooIfy(object);
    }
}

class A {
    public int foo() {
        return 1;
    }
}

class B extends A {
    public int foo() {
        return 2;
    }
}

class C extends B {
    public int foo() {
        return 3;
    }
}

class FooInator {
    public int fooIfy(A object) {
        int z;
        /* INLINE */
        z = object.foo();
        System.out.println(z);
        return z;
    }
}
