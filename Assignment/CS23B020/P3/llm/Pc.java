class Test01 {
   public static void main(String [] args) {
      A object;
      int b;
      int c;
      object = new A();
      b = 3;
      b = object.bar(b);
      c = 10;
      c = c * 4;
      c = c + b;
      System.out.println(c);
   }
}

class A {
   public int bar(int y) {
      return 6;
   }

}

class B extends A {
}

