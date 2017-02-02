package fr.hadriel.lwjgl.opengl;

import static org.lwjgl.opengl.GL15.*;

import java.nio.Buffer;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class VertexBuffer extends GLBuffer {


    public VertexBuffer(int size) {
        this(GL_STREAM_DRAW, size);
    }

    public VertexBuffer(int usage, int size) {
        super(GL_ARRAY_BUFFER, usage, size);
    }

    public VertexBuffer(int usage, Buffer buffer) {
        super(GL_ARRAY_BUFFER, usage, buffer);
    }
}