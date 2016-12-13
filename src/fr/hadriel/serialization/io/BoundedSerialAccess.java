package fr.hadriel.serialization.io;

import fr.hadriel.serialization.Serial;

import java.io.IOException;

/**
 * Created by HaDriel setOn 23/10/2016.
 */
public class BoundedSerialAccess implements SerialAccess {

    private byte[] buffer = new byte[8];
    private SerialFileChannel channel;

    private long address;
    private long size;
    private long position;

    public BoundedSerialAccess(SerialFileChannel channel, long address, long size) {
        this.channel = channel;
        this.address = address;
        this.size = size;
    }

    public long length() {
        return size;
    }

    public long remaining() {
        return position - size;
    }

    public void seek(long pointer) {
        if(pointer < 0 || pointer > size)
            throw new IllegalArgumentException("Pointer out of bounds");
        this.position = pointer;
    }

    public long getPosition() {
        return position;
    }

    private void requireSpace(int count) throws IOException {
        if(position + count > size) throw new IOException("Pointer out of bounds");
    }

    public void close() {
        channel = null;
    }

    public boolean isOpen() {
        return channel != null;
    }

    public void write(byte value) throws IOException {
        requireSpace(1);
        Serial.write(buffer, 0, value);
        channel.write(address + position, buffer, 0, 1);
        position += 1;
    }

    public void write(boolean value) throws IOException {
        requireSpace(1);
        Serial.write(buffer, 0, value);
        channel.write(address + position, buffer, 0, 1);
        position += 1;
    }

    public void write(short value) throws IOException {
        requireSpace(2);
        Serial.write(buffer, 0, value);
        channel.write(address + position, buffer, 0, 2);
        position += 2;
    }

    public void write(char value) throws IOException {
        requireSpace(2);
        Serial.write(buffer, 0, value);
        channel.write(address + position, buffer, 0, 2);
        position += 2;
    }

    public void write(int value) throws IOException {
        requireSpace(4);
        Serial.write(buffer, 0, value);
        channel.write(address + position, buffer, 0, 4);
        position += 4;
    }

    public void write(long value) throws IOException {
        requireSpace(8);
        Serial.write(buffer, 0, value);
        channel.write(address + position, buffer, 0, 8);
        position += 8;
    }

    public void write(float value) throws IOException {
        requireSpace(4);
        Serial.write(buffer, 0, value);
        channel.write(address + position, buffer, 0, 4);
        position += 4;
    }

    public void write(double value) throws IOException {
        requireSpace(8);
        Serial.write(buffer, 0, value);
        channel.write(address + position, buffer, 0, 8);
        position += 8;
    }

    public void write(byte[] bytes, int offset, int length) throws IOException {
        requireSpace(length);
        channel.write(offset + position, bytes, 0, length);
        position += length;
    }

    public void write(byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);
    }

    public byte readByte() throws IOException {
        requireSpace(1);
        channel.read(address + position, buffer, 0, 1);
        position += 1;
        return Serial.readByte(buffer, 0);
    }

    public boolean readBoolean() throws IOException {
        requireSpace(1);
        channel.read(address + position, buffer, 0, 1);
        position += 1;
        return Serial.readBoolean(buffer, 0);
    }

    public short readShort() throws IOException {
        requireSpace(2);
        channel.read(address + position, buffer, 0, 2);
        position += 2;
        return Serial.readShort(buffer, 0);
    }

    public char readChar() throws IOException {
        requireSpace(2);
        channel.read(address + position, buffer, 0, 2);
        position += 2;
        return Serial.readChar(buffer, 0);
    }

    public int readInt() throws IOException {
        requireSpace(4);
        channel.read(address + position, buffer, 0, 4);
        position += 4;
        return Serial.readInt(buffer, 0);
    }

    public long readLong() throws IOException {
        requireSpace(8);
        channel.read(address + position, buffer, 0, 8);
        position += 8;
        return Serial.readLong(buffer, 0);
    }

    public float readFloat() throws IOException {
        requireSpace(4);
        channel.read(address + position, buffer, 0, 4);
        position += 4;
        return Serial.readFloat(buffer, 0);
    }

    public double readDouble() throws IOException {
        requireSpace(8);
        channel.read(address + position, buffer, 0, 8);
        position += 8;
        return Serial.readDouble(buffer, 0);
    }

    public int readBytes(byte[] bytes, int off, int len) throws IOException {
        requireSpace(len);
        int r = channel.read(address + position, bytes, off, len);
        position += r;
        return r;
    }

    public int readBytes(byte[] buffer) throws IOException {
        return readBytes(buffer, 0, buffer.length);
    }
}
