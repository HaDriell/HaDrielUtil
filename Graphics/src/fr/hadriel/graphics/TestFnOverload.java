package fr.hadriel.graphics;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class TestFnOverload {

    private static class Base { }
    private static class Derived extends Base { }


    public static void main(String[] args) {
        Base b = new Base();
        Derived d = new Derived();
        fn(b);          // Base
        fn(d);          // Derived
        fn((Base) d);   // Base
    }

    public static void fn(Base b) {
        System.out.println("Base");
    }

    public static void fn(Derived d) {
        System.out.println("Derived");
    }
}
