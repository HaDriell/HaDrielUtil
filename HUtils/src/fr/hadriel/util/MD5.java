package fr.hadriel.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author glathuiliere
 */
public class MD5 {
    private static final MessageDigest impl;

    static {
        //Fetch the Java MD5 implementation
        try {
            impl = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Missing MD5 Implementation", e);
        }
    }

    public static byte[] digest(byte[] input) {
        impl.reset();
        return impl.digest(input);
    }
}
