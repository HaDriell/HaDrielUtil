package fr.hadriel.opengl;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class IndexBuffer {

    private final GLBuffer ibo;
    private final GLType type;
    private int count;

    public IndexBuffer(int size, GLType type) {
        this(GL_DYNAMIC_DRAW, size, type);
    }

    public IndexBuffer(int usage, int size, GLType type) {
        if(type != GLType.UBYTE && type != GLType.USHORT && type != GLType.UINT)
            throw new IllegalArgumentException("Invalid GLType Enum. Must be UBYTE, USHORT or UINT");

        this.ibo = new GLBuffer(GL_ELEMENT_ARRAY_BUFFER, usage, size);
        this.type = type;
    }

    public GLType getType() {
        return type;
    }

    public void count(int count) {
        this.count = Math.max(0, count); // can't go below 0
    }

    public int count() {
        return count;
    }


    public IndexBuffer map() {
        ibo.map();
        return this;
    }

    public IndexBuffer unmap() {
        ibo.unmap();
        return this;
    }

    public IndexBuffer bind() {
        ibo.bind();
        return this;
    }

    public IndexBuffer unbind() {
        ibo.unbind();
        return this;
    }

    public void write(byte... indices) {
        type.matches(GLType.UBYTE);
        ibo.write(indices);
        count += indices.length;
    }

    public void write(short... indices) {
        type.matches(GLType.USHORT);
        ibo.write(indices);
        count += indices.length;
    }

    public void write(int... indices) {
        type.matches(GLType.UINT);
        ibo.write(indices);
        count += indices.length;
    }

}