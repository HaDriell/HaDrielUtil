package fr.hadriel.util;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by gauti on 19/12/2017.
 */
public class Buffer {
    private static final byte FALSE = 0;
    private static final byte TRUE = 1;

    private ByteBuffer ghost; // used for nio compatibility

    protected byte[] buffer;
    protected int position;
    protected int limit;

    public Buffer(int capacity) {
        this(new byte[capacity]);
    }

    public Buffer(byte[] arrayToCopy, int offset, int length) {
        this(Arrays.copyOfRange(arrayToCopy, offset, offset + length));
    }

    public Buffer(byte[] backend) {
        wrap(backend);
    }

    public Buffer wrap(byte[] buffer) {
        this.buffer = Objects.requireNonNull(buffer);
        this.limit = buffer.length;
        this.position = 0;
        return this;
    }

    public byte[] array() {
        return buffer;
    }

    public int capacity() {
        return buffer.length;
    }

    public int position() {
        return position;
    }

    public int limit() {
        return limit;
    }

    public Buffer position(int position) {
        if(position > limit || position < 0)
            throw new IllegalArgumentException();
        this.position = position;
        return this;
    }

    public Buffer limit(int limit) {
        if(limit > buffer.length || limit < 0)
            throw new IllegalArgumentException();
        this.limit = limit;
        if(position > limit) position = limit;
        return this;
    }

    public Buffer clear() {
        position = 0;
        limit = buffer.length;
        return this;
    }

    public Buffer flip() {
        limit = position;
        position = 0;
        return this;
    }

    public int remaining() {
        return limit - position;
    }

    public Buffer write(boolean value) {
        buffer[position++] = value ? TRUE : FALSE;
        return this;
    }

    public Buffer write(byte value) {
        buffer[position++] = value;
        return this;
    }

    public Buffer write(short value) {
        buffer[position++] = (byte) ((value >> 8) & 0xFF);
        buffer[position++] = (byte) ((value >> 0) & 0xFF);
        return this;
    }

    public Buffer write(char value) {
        buffer[position++] = (byte) ((value >> 8) & 0xFF);
        buffer[position++] = (byte) ((value >> 0) & 0xFF);
        return this;
    }

    public Buffer write(int value) {
        buffer[position++] = (byte) ((value >> 24) & 0xFF);
        buffer[position++] = (byte) ((value >> 16) & 0xFF);
        buffer[position++] = (byte) ((value >> 8) & 0xFF);
        buffer[position++] = (byte) ((value >> 0) & 0xFF);
        return this;
    }

    public Buffer write(long value) {
        buffer[position++] = (byte) ((value >> 56) & 0xFF);
        buffer[position++] = (byte) ((value >> 48) & 0xFF);
        buffer[position++] = (byte) ((value >> 40) & 0xFF);
        buffer[position++] = (byte) ((value >> 32) & 0xFF);
        buffer[position++] = (byte) ((value >> 24) & 0xFF);
        buffer[position++] = (byte) ((value >> 16) & 0xFF);
        buffer[position++] = (byte) ((value >> 8) & 0xFF);
        buffer[position++] = (byte) ((value >> 0) & 0xFF);
        return this;
    }

    public Buffer write(float value) {
        return write(Float.floatToIntBits(value));
    }

    public Buffer write(double value) {
        return write(Double.doubleToLongBits(value));
    }

    public Buffer write(boolean[] array) {
        for(boolean b : array)
            write(b);
        return this;
    }

    public Buffer write(byte[] array) {
        for(byte b : array)
            write(b);
        return this;
    }

    public Buffer write(short[] array) {
        for(short b : array)
            write(b);
        return this;
    }


    public Buffer write(char[] array) {
        for(char b : array)
            write(b);
        return this;
    }


    public Buffer write(int[] array) {
        for(int b : array)
            write(b);
        return this;
    }


    public Buffer write(long[] array) {
        for(long b : array)
            write(b);
        return this;
    }


    public Buffer write(float[] array) {
        for(float b : array)
            write(b);
        return this;
    }


    public Buffer write(double[] array) {
        for(double b : array)
            write(b);
        return this;
    }

    public Buffer write(Buffer buffer) {
        byte[] source = buffer.buffer;
        int offset = buffer.position;
        int length = buffer.remaining();
        System.arraycopy(source, offset, this.buffer, this.position, length);
        buffer.position(offset + length);
        this.position += length;
        return this;
    }

    public boolean readBoolean() {
        boolean value = buffer[position] != 0;
        position += 1;
        return value;
    }

    public byte readByte() {
        byte value = buffer[position];
        position += 1;
        return value;
    }

    public short readShort() {
        short value = 0;
        value |= (buffer[position + 0] & 0xFF) << 8;
        value |= (buffer[position + 1] & 0xFF) << 0;
        position += 2;
        return value;
    }

    public char readChar() {
        char value = 0;
        value |= (buffer[position + 0] & 0xFF) << 8;
        value |= (buffer[position + 1] & 0xFF) << 0;
        position += 2;
        return value;
    }

    public int readInt() {
        int value = 0;
        value |= (buffer[position + 0] & 0xFF) << 24;
        value |= (buffer[position + 1] & 0xFF) << 16;
        value |= (buffer[position + 2] & 0xFF) << 8;
        value |= (buffer[position + 3] & 0xFF) << 0;
        position += 4;
        return value;
    }

    public long readLong() {
        long value = 0;
        value |= (buffer[position + 0] & 0xFFL) << 56;
        value |= (buffer[position + 1] & 0xFFL) << 48;
        value |= (buffer[position + 2] & 0xFFL) << 40;
        value |= (buffer[position + 3] & 0xFFL) << 32;
        value |= (buffer[position + 4] & 0xFFL) << 24;
        value |= (buffer[position + 5] & 0xFFL) << 16;
        value |= (buffer[position + 6] & 0xFFL) << 8;
        value |= (buffer[position + 7] & 0xFFL) << 0;
        position += 8;
        return value;
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public String format(String byteFormat) {
        StringBuilder out = new StringBuilder();
        for(int i = position; i < limit; i++)
            out.append(String.format(byteFormat, buffer[i]));
        return out.toString();
    }

    public ByteBuffer asByteBuffer() {
        if(ghost == null) {
            ghost = ByteBuffer.wrap(buffer);
        }
        ghost.clear();
        ghost.position(position);
        ghost.limit(limit);
        return ghost;
    }

    public Buffer[] getChunks(int chunkSize) {
        int length = remaining();
        int count = (int) Math.ceil(length / chunkSize);
        Buffer[] buffers = new Buffer[count];

        //Spliting (Buffer creation deals with System.arraycopy
        for(int i = 0; i < buffers.length; i++)
            buffers[i] = new Buffer(buffer, position + i * chunkSize, chunkSize);

        return buffers;
    }

    public static Buffer Merge(Buffer... buffers) {
        int mergeSize = 0;
        for(Buffer b : buffers)
            mergeSize += b.remaining();

        byte[] mergeArray = new byte[mergeSize];
        int offset = 0;
        for(Buffer b : buffers) {
            int bufferSize = b.remaining();
            System.arraycopy(b.buffer, b.position, mergeArray, offset, bufferSize);
            offset += bufferSize;
        }

        return new Buffer(mergeArray);
    }
}