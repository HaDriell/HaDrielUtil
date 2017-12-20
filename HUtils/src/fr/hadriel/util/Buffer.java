package fr.hadriel.util;

import fr.hadriel.serialization.Serial;

import java.util.Objects;

/**
 * Created by gauti on 19/12/2017.
 */
public class Buffer {

    private final byte[] buffer;
    private int position;
    private int save;
    private int limit;

    public Buffer(byte[] array) {
        this.buffer = Objects.requireNonNull(array);
        this.position = 0;
        this.save = -1;
        this.limit = buffer.length;
    }

    public Buffer(int capacity) {
        this(new byte[capacity]);
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

    public Buffer position(int position) {
        if(position > limit || position < 0)
            throw new IllegalArgumentException();
        this.position = position;
        if(save > position) save = -1;
        return this;
    }

    public Buffer limit(int limit) {
        if(limit > buffer.length || limit < 0)
            throw new IllegalArgumentException();
        this.limit = limit;
        if(position > limit) position = limit;
        if(save > position) save = -1;
        return this;
    }

    public Buffer save() {
        save = position;
        return this;
    }

    public Buffer load() {
        int save = this.save;
        if(save < 0)
            throw new IllegalStateException();
        position = save;
        return this;
    }

    public Buffer clear() {
        position = 0;
        limit = buffer.length;
        save = -1;
        return this;
    }

    public Buffer flip() {
        limit = position;
        position = 0;
        save = -1;
        return this;
    }

    public int remaining() {
        return limit - position;
    }

    public Buffer write(boolean value) {
        buffer[position++] = value ? Serial.TRUE :Serial.FALSE;
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

    // TODO : get rid of the Serial dependency when I've got time
    public boolean readBoolean() {
        boolean value = Serial.readBoolean(buffer, position);
        position += 1;
        return value;
    }

    public byte readByte() {
        byte value = Serial.readByte(buffer, position);
        position += 1;
        return value;
    }

    public short readShort() {
        short value = Serial.readShort(buffer, position);
        position += 2;
        return value;
    }

    public char readChar() {
        char value = Serial.readChar(buffer, position);
        position += 2;
        return value;
    }

    public int readInt() {
        int value = Serial.readInt(buffer, position);
        position += 4;
        return value;
    }

    public float readFloat() {
        float value = Serial.readFloat(buffer, position);
        position += 4;
        return value;
    }

    public long readLong() {
        long value = Serial.readLong(buffer, position);
        position += 8;
        return value;
    }

    public double readDouble() {
        double value = Serial.readDouble(buffer, position);
        position += 8;
        return value;
    }
}