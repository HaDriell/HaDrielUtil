package fr.hadriel.hgl.core;

import fr.hadriel.hgl.core.GLBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class IndexBuffer extends GLBuffer {

    private int indexType;

    public IndexBuffer(int indexType, int size) {
        this(GL_STATIC_DRAW, indexType, size);
    }

    public IndexBuffer(int usage, int indexType, int size) {
        super(GL_ELEMENT_ARRAY_BUFFER, usage, size);
        this.indexType = indexType;
//        bind();
//        switch (indexType) {
//            case GL_UNSIGNED_BYTE: setData(BufferUtils.createByteBuffer(size)); break;
//            case GL_UNSIGNED_SHORT: setData(BufferUtils.createShortBuffer(size)); break;
//            case GL_UNSIGNED_INT: setData(BufferUtils.createIntBuffer(size)); break;
//            default:
//                throw new UnsupportedOperationException("OpenGL doesn't support that index type");
//        }
//        unbind();
    }

    public int getType() {
        return indexType;
    }
}