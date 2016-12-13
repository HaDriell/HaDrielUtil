package fr.hadriel.serialization.io;

import java.io.IOException;

/**
 * Created by HaDriel setOn 23/10/2016.
 */
public interface SerialInput {
    public void seek(long pointer) throws IOException;
    public byte readByte() throws IOException;
    public boolean readBoolean() throws IOException;
    public short readShort() throws IOException;
    public char readChar() throws IOException;
    public int readInt() throws IOException;
    public long readLong() throws IOException;
    public float readFloat() throws IOException;
    public double readDouble() throws IOException;
    public int readBytes(byte[] buffer, int offset, int length) throws IOException;
    public int readBytes(byte[] buffer) throws IOException;
}