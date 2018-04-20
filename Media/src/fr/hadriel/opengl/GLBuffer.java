package fr.hadriel.opengl;

import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;
import fr.hadriel.math.Vec4;

import java.nio.*;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class GLBuffer {

    private int handle;
    private int usage;
    private int target;
    private int size;
    private ByteBuffer buffer;

    public GLBuffer(int target, int usage, int size) {
        if(size < 0) throw new IllegalArgumentException("Invalid Buffer componentSize : " + size);
        this.handle = glGenBuffers();
        this.usage = usage;
        this.target = target;
        this.size = size;
        bind();
        setData(size);
    }

    public GLBuffer(int target, int usage, Buffer buffer) {
        if(size < 0) throw new IllegalArgumentException("Invalid Buffer size : " + size);
        this.handle = glGenBuffers();
        this.usage = usage;
        this.target = target;
        this.size = buffer.remaining();
        bind();
        setData(buffer);
    }

    public void destroy() {
        glDeleteBuffers(handle);
    }

    public void setData(int size) {
        this.size = size;
        glBufferData(target, size, usage);
    }

    public void setData(Buffer buffer) {
        if(buffer instanceof ByteBuffer) {
            size = buffer.remaining();
            glBufferData(target, (ByteBuffer) buffer, usage);
        }
        else if(buffer instanceof ShortBuffer) {
            size = buffer.remaining();
            glBufferData(target, (ShortBuffer) buffer, usage);
        }
        else if(buffer instanceof IntBuffer) {
            size = buffer.remaining() * 4;
            glBufferData(target, (IntBuffer) buffer, usage);
        }
        else if(buffer instanceof FloatBuffer) {
            size = buffer.remaining() * 4;
            glBufferData(target, (FloatBuffer) buffer, usage);
        }
        else if(buffer instanceof DoubleBuffer) {
            size = buffer.remaining() * 8;
            glBufferData(target, (DoubleBuffer) buffer, usage);
        }
        else throw new IllegalArgumentException("Unsupported buffer type");
    }

    public GLBuffer bind() {
        glBindBuffer(target, handle);
        return this;
    }

    public GLBuffer unbind() {
        glBindBuffer(target, 0);
        return this;
    }

    public int getUsage() {
        return usage;
    }

    public int getSize() {
        return size;
    }

    public int getTarget() {
        return target;
    }

    public String toString() {
        return String.format("GLBuffer(handle=%d tree=%d usage=%d componentSize=%d)",handle, target, usage, size);
    }

    public GLBuffer map() {
        buffer = glMapBuffer(target, GL_READ_WRITE, buffer);
        return this;
    }

    public GLBuffer unmap() {
        glUnmapBuffer(target);
        return this;
    }

    public GLBuffer seek(int position) {
        buffer.position(position);
        return this;
    }

    public GLBuffer write(ByteBuffer value) {
        buffer.put(value);
        return this;
    }

    public GLBuffer write(byte[] value) {
        buffer.put(value);
        return this;
    }

    public GLBuffer write(short[] value) {
        for(short s : value) write(s);
        return this;
    }

    public GLBuffer write(int[] value) {
        for(int s : value) write(s);
        return this;
    }

    public GLBuffer write(long[] value) {
        for(long s : value) write(s);
        return this;
    }

    public GLBuffer write(float[] value) {
        for(float s : value) write(s);
        return this;
    }

    public GLBuffer write(double[] value) {
        for(double s : value) write(s);
        return this;
    }

    public GLBuffer write(byte value) {
        buffer.put(value);
        return this;
    }

    public GLBuffer write(short value) {
        buffer.putShort(value);
        return this;
    }

    public GLBuffer write(int value) {
        buffer.putInt(value);
        return this;
    }

    public GLBuffer write(long value) {
        buffer.putLong(value);
        return this;
    }

    public GLBuffer write(float value) {
        buffer.putFloat(value);
        return this;
    }

    public GLBuffer write(double value) {
        buffer.putDouble(value);
        return this;
    }

    public GLBuffer write(Vec2 v) {
        write(v.x).write(v.y);
        return this;
    }

    public GLBuffer write(Vec3 v) {
        write(v.x).write(v.y).write(v.z);
        return this;
    }

    public GLBuffer write(Vec4 v) {
        write(v.x).write(v.y).write(v.z).write(v.w);
        return this;
    }
}