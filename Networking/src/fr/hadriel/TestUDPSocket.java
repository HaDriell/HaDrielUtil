package fr.hadriel;

/**
 * Created by gauti on 20/12/2017.
 */
public class TestUDPSocket {

    private static final byte UNSIGNED = 0x7F;


    public static void main(String[] args) {
        byte sequence = UNSIGNED;

        for(int i = 0; i < 10_000; i++)
            System.out.println(sequence++ & UNSIGNED); // works
    }
}