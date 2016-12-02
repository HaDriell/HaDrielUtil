package fr.hadriel.hgl.core.buffers;

import fr.hadriel.hgl.core.GLType;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
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

    private IndexBuffer indexBuffer;

    public VertexArray() {
        this.bindings = new ArrayList<>();
        this.handle = glGenVertexArrays();
    }

    public void destroy() {
        if(indexBuffer != null) indexBuffer.destroy();
        for(Binding b : bindings) {
            b.vbo.destroy();
        }
        bindings.clear();
        glDeleteVertexArrays(handle);
    }

    public void bind() {
        glBindVertexArray(handle);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void draw(int mode, int elementCount) {
        if(indexBuffer == null || bindings.size() == 0) {
            System.err.println("VertexArray is deleted or not configured");
            return;
        }
        glDrawElements(mode, elementCount, GL_UNSIGNED_SHORT, 0);
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

    //Indexation setup

    public void setIndexation(IndexBuffer ibo) {
        if(indexBuffer != null) indexBuffer.unbind();
        indexBuffer = ibo;
        if(indexBuffer != null) indexBuffer.bind();
    }

    // VertexBuffer Access Methods

    public void setLayoutSubData(int index, int offset, Buffer data) {
        Binding b = get(index);
        if(b == null) return;
        b.vbo.setSubData(offset, data);
    }

    public void enableVertexLayout(int index, GLType type, int count) {
        enableVertexLayout(index, type, count, false);
    }

    public void enableVertexLayout(int index, GLType type, int components, boolean normalized) {
        disableVertexLayout(index); // will remove any existing Binding at input index
        Binding b = new Binding();
        b.index = index;
        b.pointer = new AttribPointer(type, components, normalized);
        b.vbo = new VertexBuffer(GL_DYNAMIC_DRAW, 0);
        enableVertexLayoutImpl(b);
    }

    public void disableVertexLayout(int index) {
        Binding b = get(index);
        if(b != null) disableVertexLayoutImpl(b);
    }

    private void enableVertexLayoutImpl(Binding b) {
        glEnableVertexAttribArray(b.index);
        b.vbo.bind();
        glVertexAttribPointer(b.index, b.pointer.components, b.pointer.type.name, b.pointer.normalized, 0, 0);
        bindings.add(b);
    }

    private void disableVertexLayoutImpl(Binding b) {
        bindings.remove(b);
        glDisableVertexAttribArray(b.index);
        b.vbo.destroy();
    }
}