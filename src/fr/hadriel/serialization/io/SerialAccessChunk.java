package fr.hadriel.serialization.io;

import fr.hadriel.serialization.Serial;

import java.io.IOException;

/**
 * Created by HaDriel on 23/10/2016.
 */
public class SerialAccessChunk implements SerialInput, SerialOutput {

    private byte[] buffer = new byte[8];
    private SerialAccessFile sal;

    private long pointer;
    private final long offset;
    private final long limit;


    public SerialAccessChunk(SerialAccessFile sal, long offset, long limit) {
        this.sal = sal;
        this.offset = offset;
        this.limit = limit;
    }

    public void seek(long pointer) throws IOException {
        if(pointer < 0 || pointer > limit)
            throw new IOException("Pointer out of bounds");
        this.pointer = pointer;
    }

    public long getPointer() {
        return pointer;
    }

    private void requireSpace(int count) throws IOException {
        if(pointer + count > limit) throw new IOException("Pointer out of bounds");
    }

    public void close() {
        sal = null;
    }

    public boolean isOpen() {
        return sal != null;
    }

    public void write(byte value) throws IOException {
        requireSpace(1);
        Serial.write(buffer, 0, value);
        sal.write(pointer, buffer, 0, 1);
        pointer += 1;
    }

    public void write(boolean value) throws IOException {
        requireSpace(1);
        Serial.write(buffer, 0, value);
        sal.write(pointer, buffer, 0, 1);
        pointer += 1;
    }

    public void write(short value) throws IOException {
        requireSpace(2);
        Serial.write(buffer, 0, value);
        sal.write(pointer, buffer, 0, 2);
        pointer += 2;
    }

    public void write(char value) throws IOException {
        requireSpace(2);
        Serial.write(buffer, 0, value);
        sal.write(pointer, buffer, 0, 2);
        pointer += 2;
    }

    public void write(int value) throws IOException {
        requireSpace(4);
        Serial.write(buffer, 0, value);
        sal.write(pointer, buffer, 0, 4);
        pointer += 4;
    }

    public void write(long value) throws IOException {
        requireSpace(8);
        Serial.write(buffer, 0, value);
        sal.write(pointer, buffer, 0, 8);
        pointer += 8;
    }

    public void write(float value) throws IOException {
        requireSpace(4);
        Serial.write(buffer, 0, value);
        sal.write(pointer, buffer, 0, 4);
        pointer += 4;
    }

    public void write(double value) throws IOException {
        requireSpace(8);
        Serial.write(buffer, 0, value);
        sal.write(pointer, buffer, 0, 8);
        pointer += 8;
    }

    public void write(byte[] bytes, int offset, int length) throws IOException {
        requireSpace(length);
        sal.write(pointer, bytes, 0, length);
        pointer += length;
    }

    public void write(byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);
    }

    public byte readByte() throws IOException {
        requireSpace(1);
        sal.read(pointer, buffer, 0, 1);
        pointer += 1;
        return Serial.readByte(buffer, 0);
    }

    public boolean readBoolean() throws IOException {
        requireSpace(1);
        sal.read(pointer, buffer, 0, 1);
        pointer += 1;
        return Serial.readBoolean(buffer, 0);
    }

    public short readShort() throws IOException {
        requireSpace(2);
        sal.read(pointer, buffer, 0, 2);
        pointer += 2;
        return Serial.readShort(buffer, 0);
    }

    public char readChar() throws IOException {
        requireSpace(2);
        sal.read(pointer, buffer, 0, 2);
        pointer += 2;
        return Serial.readChar(buffer, 0);
    }

    public int readInt() throws IOException {
        requireSpace(4);
        sal.read(pointer, buffer, 0, 4);
        pointer += 4;
        return Serial.readInt(buffer, 0);
    }

    public long readLong() throws IOException {
        requireSpace(8);
        sal.read(pointer, buffer, 0, 8);
        pointer += 8;
        return Serial.readLong(buffer, 0);
    }

    public float readFloat() throws IOException {
        requireSpace(4);
        sal.read(pointer, buffer, 0, 4);
        pointer += 4;
        return Serial.readFloat(buffer, 0);
    }

    public double readDouble() throws IOException {
        requireSpace(8);
        sal.read(pointer, buffer, 0, 8);
        pointer += 8;
        return Serial.readDouble(buffer, 0);
    }

    public int readBytes(byte[] bytes, int offset, int length) throws IOException {
        requireSpace(length);
        int r = sal.read(pointer, bytes, offset, length);
        pointer += r;
        return r;
    }

    public int readBytes(byte[] buffer) throws IOException {
        return readBytes(buffer, 0, buffer.length);
    }
}
