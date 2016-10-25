package fr.hadriel.serialization.hzip.old;


import fr.hadriel.serialization.Serial;

import java.io.IOException;

/**
 * Created by HaDriel on 19/10/2016.
 */
public class Space {

    private final HZipFile archive;
    private final Index index;
    private long filePointer;
    private byte[] buffer = new byte[8];

    Space(HZipFile archive, Index index) {
        this.archive = archive;
        this.index = index;
        this.filePointer = 0;
    }

    public synchronized long length() {
        return index.length();
    }

    public synchronized void seek(long position) throws IOException {
        if(position < 0 || position >= index.length()) throw new IOException("Seeked out of Space");
        filePointer = position;
    }

    public synchronized void write(byte value) throws IOException {
        Serial.write(buffer, 0, value);
        archive.write(index, filePointer, buffer, 0, 1);
        filePointer += 1;
    }

    public synchronized void write(boolean value) throws IOException {
        Serial.write(buffer, 0, value);
        archive.write(index, filePointer, buffer, 0, 1);
        filePointer += 1;
    }

    public synchronized void write(short value) throws IOException {
        Serial.write(buffer, 0, value);
        archive.write(index, filePointer, buffer, 0, 2);
        filePointer += 2;
    }

    public synchronized void write(char value) throws IOException {
        Serial.write(buffer, 0, value);
        archive.write(index, filePointer, buffer, 0, 2);
        filePointer += 2;
    }

    public synchronized void write(int value) throws IOException {
        Serial.write(buffer, 0, value);
        archive.write(index, filePointer, buffer, 0, 4);
        filePointer += 4;
    }

    public synchronized void write(long value) throws IOException {
        Serial.write(buffer, 0, value);
        archive.write(index, filePointer, buffer, 0, 8);
        filePointer += 8;
    }

    public synchronized void write(float value) throws IOException {
        Serial.write(buffer, 0, value);
        archive.write(index, filePointer, buffer, 0, 4);
        filePointer += 4;
    }

    public synchronized void write(double value) throws IOException {
        Serial.write(buffer, 0, value);
        archive.write(index, filePointer, buffer, 0, 8);
        filePointer += 8;
    }

    public synchronized void write(byte[] buffer, int offset, int length) throws IOException {
        archive.write(index, filePointer, buffer, offset, length);
        filePointer += length;
    }

    public synchronized void write(byte[] buffer) throws IOException {
        archive.write(index, filePointer, buffer, 0, buffer.length);
        filePointer += buffer.length;
    }

    public synchronized byte readByte() throws IOException {
        archive.read(index, filePointer, buffer, 0, 1);
        filePointer += 1;
        return Serial.readByte(buffer, 0);
    }

    public synchronized boolean readBoolean() throws IOException {
        archive.read(index, filePointer, buffer, 0, 1);
        filePointer += 1;
        return Serial.readBoolean(buffer, 0);
    }

    public synchronized short readShort() throws IOException {
        archive.read(index, filePointer, buffer, 0, 2);
        filePointer += 2;
        return Serial.readShort(buffer, 0);
    }

    public synchronized char readChar() throws IOException {
        archive.read(index, filePointer, buffer, 0, 2);
        filePointer += 2;
        return Serial.readChar(buffer, 0);
    }

    public synchronized int readInt() throws IOException {
        archive.read(index, filePointer, buffer, 0, 4);
        filePointer += 4;
        return Serial.readInt(buffer, 0);
    }

    public synchronized long readLong() throws IOException {
        archive.read(index, filePointer, buffer, 0, 8);
        filePointer += 8;
        return Serial.readLong(buffer, 0);
    }

    public synchronized float readFloat() throws IOException {
        archive.read(index, filePointer, buffer, 0, 4);
        filePointer += 4;
        return Serial.readByte(buffer, 0);
    }

    public synchronized double readDouble() throws IOException {
        archive.read(index, filePointer, buffer, 0, 8);
        filePointer += 8;
        return Serial.readByte(buffer, 0);
    }
}