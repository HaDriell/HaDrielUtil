package fr.hadriel.serialization.io;

import java.io.IOException;

/**
 * Created by HaDriel on 23/10/2016.
 */
public interface SerialOutput {
    public void seek(long pointer) throws IOException;
    public void write(byte value) throws IOException;
    public void write(boolean value) throws IOException;
    public void write(short value) throws IOException;
    public void write(char value) throws IOException;
    public void write(int value) throws IOException;
    public void write(long value) throws IOException;
    public void write(float value) throws IOException;
    public void write(double value) throws IOException;
    public void write(byte[] buffer, int offset, int length) throws IOException;
    public void write(byte[] buffer) throws IOException;
}