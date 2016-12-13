package fr.hadriel.util;

/**
 * Created by HaDriel setOn 21/10/2016.
 *
 * Implementation of Mersenne Twister PRNG
 * Source: https://cs.gmu.edu/~sean/research/mersenne/MersenneTwisterFast.java
 */
public class PRNG {

    private static final byte A = 1;
    private static final byte B = 7;
    private static final byte C = 9;

    private long seed;

    public PRNG(long seed) {
        this.seed = seed;
    }

    public PRNG() {
        this(System.nanoTime());
    }

    private long xor() {
        seed ^= seed << A;
        seed ^= seed >> B;
        seed ^= seed << C;
        return seed & Long.MAX_VALUE; // unsign the output
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    // random getters

    public long nextLong() {
        return xor();
    }

    public int nextInt() {
        return (int) (xor() >> 32);
    }

    public short nextShort() {
        return (short) (xor() >> 48);
    }

    public char nextChar() {
        return (char) (xor() >> 48);
    }

    public byte nextByte() {
        return (byte) (xor() >> 56);
    }

    public boolean nextBoolean() {
        return xor() >> 63 != 0;
    }

    public float nextFloat() {
        return nextInt() / (float) Integer.MAX_VALUE;
    }

    public double nextDouble() {
        return nextLong() / (double) Long.MAX_VALUE;
    }

    public void nextBytes(byte[] buffer, int offset, int length) {
        for(int i = offset; i < offset + length; i++)
            buffer[i] = nextByte();
    }
}