package fr.hadriel.hgl.core.buffers;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class IndexBuffer extends GLBuffer {

    public IndexBuffer(int size) {
        this(GL_STATIC_DRAW, size);
    }

    public IndexBuffer(int usage, int size) {
        super(GL_ELEMENT_ARRAY_BUFFER, usage, size);
    }
}