package fr.hadriel.serialization.io;

import fr.hadriel.serialization.Serial;
import fr.hadriel.util.memory.Pointer;

import java.io.IOException;

/**
 * Created by HaDriel on 23/10/2016.
 */
public class PointerSerialAccess implements SerialAccess {

    private byte[] buffer = new byte[8];
    private SerialFileChannel channel;

    private Pointer p;
    private long position;
    private final boolean allowInsert;

    public PointerSerialAccess(SerialFileChannel channel, Pointer p, boolean allowInsert) {
        this.channel = channel;
        this.p = p;
        this.allowInsert = allowInsert;
    }

    public PointerSerialAccess(SerialFileChannel channel, Pointer p) {
        this(channel, p, true);
    }

    private void checkPointer() throws IllegalStateException {
        if(!p.isInitialized()) throw new IllegalStateException("Invalid Pointer.");
    }

    public long length() {
        checkPointer();
        return p.size();
    }

    public long remaining() {
        checkPointer();
        return position - p.size();
    }

    public void seek(long pointer) {
        checkPointer();
        if(pointer < 0 || pointer > p.size())
            throw new IllegalArgumentException("Pointer out of bounds");
        this.position = pointer;
    }

    public long getPosition() {
        checkPointer();
        return position;
    }

    private void requireSpace(int count, boolean writing) throws IOException {
        checkPointer();
        if(writing && allowInsert) {
            long delta = position + count - p.size();
            if (delta > 0) p.resize(p.size() + delta);
        }
        if(position + count > p.size()) {
            throw new IOException("Pointer out of bounds");
        }
    }

    public void close() {
        channel = null;
    }

    public boolean isOpen() {
        return channel != null && p.isInitialized();
    }

    public void write(byte value) throws IOException {
        requireSpace(1, true);
        Serial.write(buffer, 0, value);
        channel.write(p.address() + position, buffer, 0, 1);
        position += 1;
    }

    public void write(boolean value) throws IOException {
        requireSpace(1, true);
        Serial.write(buffer, 0, value);
        channel.write(p.address() + position, buffer, 0, 1);
        position += 1;
    }

    public void write(short value) throws IOException {
        requireSpace(2, true);
        Serial.write(buffer, 0, value);
        channel.write(p.address() + position, buffer, 0, 2);
        position += 2;
    }

    public void write(char value) throws IOException {
        requireSpace(2, true);
        Serial.write(buffer, 0, value);
        channel.write(p.address() + position, buffer, 0, 2);
        position += 2;
    }

    public void write(int value) throws IOException {
        requireSpace(4, true);
        Serial.write(buffer, 0, value);
        channel.write(p.address() + position, buffer, 0, 4);
        position += 4;
    }

    public void write(long value) throws IOException {
        requireSpace(8, true);
        Serial.write(buffer, 0, value);
        channel.write(p.address() + position, buffer, 0, 8);
        position += 8;
    }

    public void write(float value) throws IOException {
        requireSpace(4, true);
        Serial.write(buffer, 0, value);
        channel.write(p.address() + position, buffer, 0, 4);
        position += 4;
    }

    public void write(double value) throws IOException {
        requireSpace(8, true);
        Serial.write(buffer, 0, value);
        channel.write(p.address() + position, buffer, 0, 8);
        position += 8;
    }

    public void write(byte[] bytes, int offset, int length) throws IOException {
        requireSpace(length, true);
        channel.write(offset + position, bytes, 0, length);
        position += length;
    }

    public void write(byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);
    }

    public byte readByte() throws IOException {
        requireSpace(1, false);
        channel.read(p.address() + position, buffer, 0, 1);
        position += 1;
        return Serial.readByte(buffer, 0);
    }

    public boolean readBoolean() throws IOException {
        requireSpace(1, false);
        channel.read(p.address() + position, buffer, 0, 1);
        position += 1;
        return Serial.readBoolean(buffer, 0);
    }

    public short readShort() throws IOException {
        requireSpace(2, false);
        channel.read(p.address() + position, buffer, 0, 2);
        position += 2;
        return Serial.readShort(buffer, 0);
    }

    public char readChar() throws IOException {
        requireSpace(2, false);
        channel.read(p.address() + position, buffer, 0, 2);
        position += 2;
        return Serial.readChar(buffer, 0);
    }

    public int readInt() throws IOException {
        requireSpace(4, false);
        channel.read(p.address() + position, buffer, 0, 4);
        position += 4;
        return Serial.readInt(buffer, 0);
    }

    public long readLong() throws IOException {
        requireSpace(8, false);
        channel.read(p.address() + position, buffer, 0, 8);
        position += 8;
        return Serial.readLong(buffer, 0);
    }

    public float readFloat() throws IOException {
        requireSpace(4, false);
        channel.read(p.address() + position, buffer, 0, 4);
        position += 4;
        return Serial.readFloat(buffer, 0);
    }

    public double readDouble() throws IOException {
        requireSpace(8, false);
        channel.read(p.address() + position, buffer, 0, 8);
        position += 8;
        return Serial.readDouble(buffer, 0);
    }

    public int readBytes(byte[] bytes, int off, int len) throws IOException {
        requireSpace(len, false);
        int r = channel.read(p.address() + position, bytes, off, len);
        position += r;
        return r;
    }

    public int readBytes(byte[] buffer) throws IOException {
        return readBytes(buffer, 0, buffer.length);
    }
}