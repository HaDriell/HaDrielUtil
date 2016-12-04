package fr.hadriel.hgl.opengl;

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

    public void bind() {
        glBindBuffer(target, handle);
    }

    public void unbind() {
        glBindBuffer(target, 0);
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
        return String.format("GLBuffer(handle=%d target=%d usage=%d componentSize=%d)",handle, target, usage, size);
    }
}