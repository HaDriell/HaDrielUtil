package fr.hadriel.hgl.core.buffers;

import fr.hadriel.hgl.core.GLType;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
/**
 * Created by HaDriel on 30/11/2016.
 */
public class VertexArray {

    private static class Binding {
        public int index;
        public AttribPointer pointer;
        public VertexBuffer vbo;
    }

    private int handle;
    private List<Binding> bindings;

    private int maxElementCount;
    private IndexBuffer indexBuffer;

    public VertexArray(int maxElementCount) {
        this(IndexGenerator.TRIANGLES, maxElementCount);
    }

    public VertexArray(IndexGenerator generator, int maxElementCount) {
        this.maxElementCount = maxElementCount;
        this.bindings = new ArrayList<>();
        this.handle = glGenVertexArrays();
        ShortBuffer indices = generator.getIndexBuffer(maxElementCount);
        bind();
        indexBuffer = new IndexBuffer(GL15.GL_STATIC_DRAW, indices.remaining());
        indexBuffer.setData(indices);
        unbind();
    }

    public void destroy() {
        for(Binding b : bindings) {
            b.vbo.destroy();
        }
        bindings.clear();
        indexBuffer.destroy();
        glDeleteVertexArrays(handle);
    }

    public int getMaxElementCount() {
        return maxElementCount;
    }

    public void bind() {
        glBindVertexArray(handle);
    }


    public void unbind() {
        glBindVertexArray(0);
    }

    public void draw(int elementCount) {
        bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, elementCount, GL11.GL_UNSIGNED_SHORT, 0);
        unbind();
    }

    //Managed VertexBuffer API

    private Binding get(int index) {
        Binding binding = null;
        for(Binding b : bindings) {
            if(b.index == index) {
                binding = b;
                break;
            }
        }
        return binding;
    }

    public void setLayoutData(int index, ByteBuffer data) {
        Binding b = get(index);
        if(b == null) return;
        b.vbo.setData(data);
    }

    public void enableVertexLayout(int index, GLType type, int count, boolean normalized) {
        disableVertexLayout(index);
        Binding b = new Binding();
        b.index = index;
        b.pointer = new AttribPointer(type, count, normalized);
        b.vbo = new VertexBuffer(GL15.GL_DYNAMIC_DRAW, b.pointer.getAttribSize() * maxElementCount);
        enableVertexLayoutImpl(b);
    }

    public void disableVertexLayout(int index) {
        Binding b = get(index);
        if(b != null) disableVertexLayoutImpl(b);
    }

    private void enableVertexLayoutImpl(Binding b) {
        GL20.glEnableVertexAttribArray(b.index);
        bindings.add(b);
    }

    private void disableVertexLayoutImpl(Binding b) {
        bindings.remove(b);
        GL20.glDisableVertexAttribArray(b.index);
        b.vbo.destroy();
    }
}