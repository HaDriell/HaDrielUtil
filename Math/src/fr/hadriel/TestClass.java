package fr.hadriel;

/**
 *
 * @author glathuiliere
 */
public class TestClass {
    public static class A {}
    public static class B extends A {}

    public static void main(String[] args) {
        A a = new B();
        System.out.println(a.getClass());
    }
}
