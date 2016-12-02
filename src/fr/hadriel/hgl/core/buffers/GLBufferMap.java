package fr.hadriel.hgl.core.buffers;


import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15.*;


/**
 * Created by HaDriel on 02/12/2016.
 */
public class GLBufferMap {

    //TODO : merge with GLBuffer and get a more granular API (write should map & unmap atomically)

    private GLMode mode;
    private int target;
    private ByteBuffer buffer; // will be setup after a first call to open() of a given VBO

    public void open(int target, int size, GLMode mode) {
        if(this.mode != null) close();
        if(mode == null) throw new IllegalArgumentException("Must provide an Access mode for that Buffer");
        this.mode = mode;
        this.target = target;
        this.buffer = glMapBuffer(target,mode.mode, size, buffer);
    }

    public void close() {
        if(mode == null) return;
        glUnmapBuffer(target);
        buffer.clear();
        mode = null;
    }

    private void require(GLMode required) {
        if(mode != required && mode != GLMode.BOTH) throw new IllegalStateException("Cannot Read/Write from Buffer : no access");
    }

    public void seek(int pointer) {
        buffer.position(pointer);
    }

    public int position() {
        return buffer.position();
    }

    public GLBufferMap write(byte value) {
        require(GLMode.WRITE);
        buffer.put(value);
        return this;
    }

    public GLBufferMap write(boolean value) {
        require(GLMode.WRITE);
        buffer.put((byte) (value ? 1 : 0));
        return this;
    }

    public GLBufferMap write(short value) {
        require(GLMode.WRITE);
        buffer.putShort(value);
        return this;
    }

    public GLBufferMap write(int value) {
        require(GLMode.WRITE);
        buffer.putInt(value);
        return this;
    }

    public GLBufferMap write(long value) {
        require(GLMode.WRITE);
        buffer.putLong(value);
        return this;
    }

    public GLBufferMap write(float value) {
        require(GLMode.WRITE);
        buffer.putFloat(value);
        return this;
    }

    public GLBufferMap write(double value) {
        require(GLMode.WRITE);
        buffer.putDouble(value);
        return this;
    }

    public GLBufferMap write(byte[] bytes) {
        require(GLMode.WRITE);
        buffer.put(bytes);
        return this;
    }

    public GLBufferMap write(ByteBuffer byteBuffer) {
        require(GLMode.WRITE);
        buffer.put(byteBuffer);
        return this;
    }

    public byte readByte() {
        require(GLMode.READ);
        return buffer.get();
    }

    public short readShort() {
        require(GLMode.READ);
        return buffer.getShort();
    }

    public int readInt() {
        require(GLMode.READ);
        return buffer.getInt();
    }

    public long readLong() {
        require(GLMode.READ);
        return buffer.getLong();
    }

    public float readFloat() {
        require(GLMode.READ);
        return buffer.getFloat();
    }

    public double readDouble() {
        require(GLMode.READ);
        return buffer.getDouble();
    }
}