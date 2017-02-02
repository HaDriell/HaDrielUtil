package fr.hadriel.lwjgl.opengl;

import java.nio.ShortBuffer;

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

    public IndexBuffer(int usage, ShortBuffer indices) {
        super(GL_ELEMENT_ARRAY_BUFFER, usage, indices);
    }

    public IndexBuffer(ShortBuffer indices) {
        super(GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, indices);
    }
}