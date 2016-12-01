package fr.hadriel.hgl.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

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
        this.size = size;
        this.target = target;
        this.usage = usage;
        this.handle = glGenBuffers();
    }

    public void destroy() {
        glDeleteBuffers(handle);
    }

    public void resize(int nsize) {
        size = nsize;
        glBufferData(target, size, usage);
    }

    private void ensureSize(int nsize) {
        if(nsize > size)
            resize(nsize);
    }

    public void setData(ByteBuffer data) {
        ensureSize(data.remaining());
        glBufferSubData(target, 0, data);
    }

    public void setData(ShortBuffer data) {
        ensureSize(data.remaining());
        glBufferSubData(target, 0, data);
    }

    public void setData(IntBuffer data) {
        ensureSize(data.remaining());
        glBufferSubData(target, 0, data);
    }

    public void setData(FloatBuffer data) {
        ensureSize(data.remaining());
        glBufferSubData(target, 0, data);
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
        return String.format("GLBuffer(handle=%d target=%d usage=%d size=%d)",handle, target, usage, size);
    }
}