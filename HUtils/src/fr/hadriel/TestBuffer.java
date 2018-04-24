package fr.hadriel;

import fr.hadriel.util.Buffer;

import java.util.Random;

import static fr.hadriel.util.Assert.*;

/**
 *
 * @author glathuiliere
 */
public class TestBuffer {

    public static void main(String... args) {
        Buffer buffer = new Buffer(8);
        Random random = new Random();

        boolean a   = random.nextBoolean();
        byte b      = (byte) random.nextInt();
        short c     = (short) random.nextInt();
        char d      = (char) random.nextInt();
        int e       = random.nextInt();
        long f      = random.nextLong();
        float g     = random.nextFloat();
        double h    = random.nextDouble();

        assertEqual(buffer.clear().write(a).flip().readBoolean(), a, "Failed to read/write Primitive");
        assertEqual(buffer.clear().write(b).flip().readByte()   , b, "Failed to read/write Primitive");
        assertEqual(buffer.clear().write(c).flip().readShort()  , c, "Failed to read/write Primitive");
        assertEqual(buffer.clear().write(d).flip().readChar()   , d, "Failed to read/write Primitive");
        assertEqual(buffer.clear().write(e).flip().readInt()    , e, "Failed to read/write Primitive");
        assertEqual(buffer.clear().write(f).flip().readLong()   , f, "Failed to read/write Primitive");
        assertEqual(buffer.clear().write(g).flip().readFloat()  , g, "Failed to read/write Primitive");
        assertEqual(buffer.clear().write(h).flip().readDouble() , h, "Failed to read/write Primitive");
    }
}