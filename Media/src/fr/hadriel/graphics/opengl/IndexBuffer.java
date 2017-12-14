package fr.hadriel.graphics.opengl;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class IndexBuffer extends GLBuffer {

    private GLType indexType;

    public IndexBuffer(int size, GLType indexType) {
        this(GL_DYNAMIC_DRAW, size, indexType);
    }

    public IndexBuffer(int usage, int size, GLType indexType) {
        super(GL_ELEMENT_ARRAY_BUFFER, usage, size);
        setIndexType(indexType);
    }

    public void setIndexType(GLType indexType) {
        if(indexType != GLType.UBYTE && indexType != GLType.USHORT && indexType != GLType.UINT)
            throw new IllegalArgumentException("Invalid GLType Enum. Must be UBYTE, USHORT or UINT");
        this.indexType = indexType;
    }

    public GLType getIndexType() {
        return indexType;
    }
}