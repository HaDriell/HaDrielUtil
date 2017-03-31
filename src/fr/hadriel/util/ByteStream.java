package fr.hadriel.util;

import java.util.Arrays;

/**
 * Created by glathuiliere on 07/11/2016.
 */
public class ByteStream {

    private byte[] stream;
    private int shift;
    private int size;

    public ByteStream() {
        this(1024);
    }

    public ByteStream(int capacity) {
        stream = new byte[capacity];
        size = 0;
    }

    public int available() {
        return size;
    }

    public void unshift() {
        if(shift == 0) return;
        stream = Arrays.copyOfRange(stream, shift, stream.length);
    }

    public void ensureCapacity(int nsize) {
        if(nsize > stream.length) {
            System.out.println("resizing to " + nsize);
            //new componentSize is over getEntityStream length, grow the array
            while (nsize > stream.length) {
                stream = Arrays.copyOf(stream, (int) (stream.length * 1.7F));
            }
        } else if(nsize + shift > stream.length) {
            System.out.println("unshifting");
            //new componentSize is not over capacity BUT unshifting is mandatory to avoid overflow
            byte[] nstream = new byte[stream.length];
            System.arraycopy(stream, shift, nstream, 0, size);
            stream = nstream;
            shift = 0;
        }
        //Overwise do nothing, it will fit
    }

    public void write(byte[] buffer, int offset, int length) {
        ensureCapacity(length + available());
        System.arraycopy(buffer, offset, stream, shift + size, length);
        size += length;
    }

    public int peek(byte[] buffer, int offset, int length) {
        int read = Math.min(size, length);
        System.arraycopy(stream, shift, buffer, offset, read);
        return read;
    }

    public void discard(int length) {
        shift += length;
        size -= length;
    }

    public int read(byte[] buffer, int offset, int length) {
        int read = peek(buffer, offset, length);
        discard(read);
        return read;
    }

    public int peek(byte[] buffer) {
        return peek(buffer, 0, buffer.length);
    }

    public void write(byte[] buffer) {
        write(buffer, 0, buffer.length);
    }

    public int read(byte[] buffer) {
        return read(buffer, 0, buffer.length);
    }

    public void printStatus() {
        System.out.println(String.format("Buffer<off=%d, len=%d, cpa=%d>", shift, size, stream.length));
    }
}