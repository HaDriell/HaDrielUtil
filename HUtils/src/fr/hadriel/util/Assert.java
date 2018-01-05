package fr.hadriel.util;

/**
 *
 * @author glathuiliere
 */
public final class Assert {
    private Assert() {}

    private static void fail(String message) {
        throw new RuntimeException(message);
    }

    private static void fail(String message, Throwable t) {
        throw new RuntimeException(message, t);
    }

    public static void assertEqual(Object a, Object b, String message) {
        if(!a.equals(b))
            fail(message + "(" + a + " != " + b + ")");
    }

    public static void assertNotEqual(Object a, Object b, String message) {
        if(a.equals(b))
            fail(message);
    }

    public static void assertTrue(boolean value, String message) {
        if(!value) fail(message);
    }

    public static void assertFalse(boolean value, String message) {
        assertTrue(!value, message);
    }

    public static void expect(Class<? extends Throwable> exceptionType, Runnable code) {
        try {
            code.run();
        } catch (Throwable t) {
            if (exceptionType.isInstance(t))
                return;
            else
                fail("Expected " + exceptionType.getName() + " but had " + t.getClass().getName() + " instead");
        }
        fail("Expected " + exceptionType.getName() + " but no Error was thrown.");
    }
}
