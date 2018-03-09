package fr.hadriel.opengl;



import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
/**
 * Created by glathuiliere on 06/12/2016.
 */
public final class VertexArray {

    private final int handle;

    //Properties
    private VertexAttribute[] vertexAttributes;
    private int maxElementCount;
    private VertexBuffer vertexBuffer;

    public VertexArray(int maxElementCount, VertexAttribute[] vertexAttributes) {
        this.handle = glGenVertexArrays();
        this.vertexAttributes = vertexAttributes;
        this.maxElementCount = maxElementCount;
        this.vertexBuffer = new VertexBuffer(maxElementCount * sizeof(vertexAttributes));
        setupAttributes();
    }

    public void setMaxElementCount(int maxElementCount) {
        if(vertexBuffer != null) vertexBuffer.destroy();
        this.maxElementCount = maxElementCount;
        vertexBuffer = new VertexBuffer(maxElementCount * sizeof(vertexAttributes));
        setupAttributes();
    }

    public void setVertexAttributes(VertexAttribute[] vertexAttributes) {
        this.vertexAttributes = Objects.requireNonNull(vertexAttributes);
        setupAttributes();
    }

    private void setupAttributes() {
        int stride = sizeof(vertexAttributes);

        if(vertexBuffer.getSize() != maxElementCount * stride)
            throw new RuntimeException("VertexLayout is not aligned with the VertexBuffer size");

        bind();
        vertexBuffer.bind();
        int offset = 0;
        for(int i = 0; i < vertexAttributes.length; i++) {
            VertexAttribute attribute = vertexAttributes[i];
            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, attribute.components, attribute.type.name, attribute.normalized, stride, offset);
            offset += attribute.type.size * attribute.components;
        }
        vertexBuffer.unbind();
        unbind();
    }

    public void destroy() {
        vertexBuffer.destroy();
        glDeleteVertexArrays(handle);
    }

    public VertexBuffer getBuffer() {
        return vertexBuffer;
    }

    public void bind() {
        glBindVertexArray(handle);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public int getMaxElementCount() {
        return maxElementCount;
    }

    private static int sizeof(VertexAttribute[] vertexAttributes) {
        int size = 0;
        for(VertexAttribute attribute : vertexAttributes)
            size += attribute.type.size * attribute.components;
        return size;
    }
}