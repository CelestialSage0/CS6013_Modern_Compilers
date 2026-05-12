class TestObjSensHelper {
    public static void main (String[] args) {
        Helper h;
        Box b1;
        Box b2;
        ItemA a;
        ItemB bi;
        Item r1g;
        Item r2g;
        int junk;
        h = new Helper ();
        b1 = new Box ();
        b2 = new Box ();
        a = new ItemA ();
        bi = new ItemB ();
        junk = h.store (b1, a);
        junk = h.store (b2, bi);
        r1g = b1.get ();
        r2g = b2.get ();
        junk = r1g.work ();
        junk = r2g.work ();
        System.out.println (junk);
    }
}
class Helper {
    public int store (Box b, Item it) {
        b.item = it;
        return 0;
    }
}
class Box {
    Item item;
    public Item get () {
        Item r;
        r = item;
        return r;
    }
}
class Item {
    public int work () {
        return 1;
    }
}
class ItemA extends Item {
    public int work () {
        return 2;
    }
}
class ItemB extends Item {
    public int work () {
        return 3;
    }
}
