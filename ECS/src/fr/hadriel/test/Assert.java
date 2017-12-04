package fr.hadriel.test;

public class Assert {
    public static void assertTrue(boolean value, String message) {
        if(!value) throw new RuntimeException(message);
    }

    public static void assertFalse(boolean value, String message) {
        assertTrue(!value, message);
    }

    public static void assertEqual(Object a, Object b, String message) {
        assertTrue(a.equals(b), message);
    }

    public static void assertNotEqual(Object a, Object b, String message) {
        assertTrue(!a.equals(b), message);
    }
}
