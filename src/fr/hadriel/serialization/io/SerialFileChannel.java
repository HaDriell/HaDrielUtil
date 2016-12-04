package fr.hadriel.serialization.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * Created by HaDriel on 23/10/2016.
 */
public class SerialFileChannel {

    private FileChannel channel;
    private byte[] copyBuffer = new byte[1024 * 1024]; // 1MB copy buffer

    public SerialFileChannel(String filename) throws IOException {
        this(new File(filename));
    }

    public SerialFileChannel(File file) throws IOException {
        channel = FileChannel.open(file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

    public boolean isOpen() {
        return channel != null;
    }

    public synchronized void close() {
        try { channel.close(); } catch (IOException ignore) {} // try to close gracefully
        channel = null;
    }

    private synchronized void requireOpen() throws IOException {
        if(!isOpen()) throw new IOException("not opened");
    }

    public synchronized long length() throws IOException {
        requireOpen();
        return channel.size();
    }

    public synchronized void setLength(long length) throws IOException {
        requireOpen();
        long size = channel.size();
        long delta = length - size;
        if(delta > 0) {
            channel.position(size);
            channel.write(ByteBuffer.wrap(copyBuffer, 0, (int) delta));
        }
        if(delta < 0) {
            channel.truncate(length);
        }
    }

    public synchronized void write(long pointer, byte[] buffer, int offset, int length) throws IOException {
        requireOpen();
        channel.position(pointer);
        channel.write(ByteBuffer.wrap(buffer, offset, length));
    }

    public synchronized int read(long pointer, byte[] buffer, int offset, int length) throws IOException {
        requireOpen();
        channel.position(pointer);
        return channel.read(ByteBuffer.wrap(buffer, offset, length));
    }

    public synchronized void write(long pointer, byte[] buffer) throws IOException {
        write(pointer, buffer, 0, buffer.length);
    }

    public synchronized int read(long pointer, byte[] buffer) throws IOException {
        return read(pointer, buffer, 0, buffer.length);
    }


    public synchronized void insert(long pointer, long count) throws IOException {
        requireOpen();
        long position = channel.size();
        setLength(channel.size() + count);
        while(position > pointer) {
            int size = (int) Math.min(position - pointer, copyBuffer.length); // find the buffer componentSize to use
            read(position - size, copyBuffer, 0, size);
            write(position - size + count, copyBuffer, 0, size);
            position -= size; //shift position back
        } //continue
    }

    public synchronized void remove(long pointer, long count) throws IOException {
        requireOpen();
        long position = pointer + count;
        long length = channel.size();
        while (length > position) {
            int size = (int) Math.min(length - position, copyBuffer.length);
            read(position, copyBuffer, 0, size);
            write(position - count, copyBuffer, 0, size);
            position += size;
        }
        setLength(length - count);
    }
}