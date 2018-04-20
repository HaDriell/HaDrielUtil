package fr.hadriel.opengl;

import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;
import fr.hadriel.math.Vec4;

import javax.xml.bind.DatatypeConverter;
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
        buffer.clear();
        return this;
    }

    public void debug() {
        int position = buffer.position();
        buffer.position(0);

        byte[] data = new byte[4 * 8 * 9 * 4];
        buffer.get(data, 0, data.length);


        int id = 0;
        int o = 0;
        byte[] vpos = new byte[4 * 2];
        byte[] vcolor = new byte[4 * 4];
        byte[] vuv = new byte[4 * 2];
        byte[] vtid = new byte[4];
        while (o < data.length) {
            System.arraycopy(data, o, vpos, 0, vpos.length); o += vpos.length;
            System.arraycopy(data, o, vcolor, 0, vcolor.length); o += vcolor.length;
            System.arraycopy(data, o, vuv, 0, vuv.length); o += vuv.length;
            System.arraycopy(data, o, vtid, 0, vtid.length); o += vtid.length;
            System.out.println(String.format("Vertex %d %s %s %s %s",
                    id,
                    DatatypeConverter.printHexBinary(vpos),
                    DatatypeConverter.printHexBinary(vcolor),
                    DatatypeConverter.printHexBinary(vuv),
                    DatatypeConverter.printHexBinary(vtid)
            ));
            id++;
        }



        buffer.position(position);
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