package fr.hadriel.hgl.core;

import org.lwjgl.opengl.GL15;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class VertexBuffer extends GLBuffer {


    public VertexBuffer(int size) {
        this(GL15.GL_STREAM_DRAW, size);
    }

    public VertexBuffer(int usage, int size) {
        super(GL15.GL_ARRAY_BUFFER, usage, size);
    }
}