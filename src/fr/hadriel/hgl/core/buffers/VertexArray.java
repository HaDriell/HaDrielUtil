package fr.hadriel.hgl.core.buffers;

import fr.hadriel.util.Property;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
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

    private Property<Integer> maxElementCountProperty;
    private Property<IndexGenerator> generatorProperty;

    public VertexArray() {
        this(1024);
    }

    public VertexArray(int elementCount) {
        this(elementCount, IndexGenerator.TRIANGLES);
    }

    public VertexArray(int maxElementCount, IndexGenerator indexGenerator) {
        this.maxElementCountProperty = new Property<>(maxElementCount, 1);
        this.generatorProperty = new Property<>();
        this.bindings = new ArrayList<>();
        this.handle = glGenVertexArrays();
        this.maxElementCountProperty.addCallback((nval) -> {
            for(Binding b : bindings) {
                b.vbo.setData(b.pointer.getLayoutSize() * nval);
            }
        });
        this.maxElementCountProperty.addCallback((nval) -> {
            IndexGenerator generator = generatorProperty.get();

            //Indexation is enabled, regenerate indices for nval Elements support
            if(generator != null) indexBuffer.setData(generator.getIndexBuffer(nval));
        });
        this.generatorProperty.addCallback((nval) -> {
            if(nval == null) {
                indexBuffer.destroy();
                indexBuffer = null;
            } else {
                bind();
                indexBuffer = new IndexBuffer(nval.getIndexBuffer(maxElementCountProperty.get()));
            }
        });
        this.generatorProperty.set(indexGenerator);
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

    public Property<IndexGenerator> generator() {
        return generatorProperty;
    }

    public Property<Integer> maxElementCount() {
        return maxElementCountProperty;
    }

    // VertexBuffer Access Methods

    public GLBufferMap getBufferMap(int index, GLMode mode) {
        Binding b = get(index);
        if(b == null) return null;
        b.vbo.bind();
        return b.vbo.open(mode);
    }

    public void enableVertexLayout(int index, GLType type, int count) {
        enableVertexLayout(index, type, count, false);
    }

    public void enableVertexLayout(int index, GLType type, int components, boolean normalized) {
        disableVertexLayout(index); // will remove any existing Binding at input index
        Binding b = new Binding();
        b.index = index;
        b.pointer = new AttribPointer(type, components, normalized);
        b.vbo = new VertexBuffer(b.pointer.getLayoutSize() * maxElementCountProperty.get());
        enableVertexLayoutImpl(b);
    }

    public void disableVertexLayout(int index) {
        Binding b = get(index);
        if(b != null) disableVertexLayoutImpl(b);
    }

    private void enableVertexLayoutImpl(Binding b) {
        b.vbo.bind();
        glEnableVertexAttribArray(b.index);
        glVertexAttribPointer(b.index, b.pointer.components, b.pointer.type.name, b.pointer.normalized, 0, 0);
        bindings.add(b);
    }

    private void disableVertexLayoutImpl(Binding b) {
        glDisableVertexAttribArray(b.index);
        b.vbo.destroy();
        bindings.remove(b);
    }
}