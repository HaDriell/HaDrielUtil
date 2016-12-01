package fr.hadriel.hgl.core.gl3;

import fr.hadriel.hgl.core.IndexBuffer;
import fr.hadriel.hgl.core.IndexGenerator;
import fr.hadriel.hgl.core.VertexBuffer;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
/**
 * Created by HaDriel on 30/11/2016.
 */
public abstract class VertexArray {

    private static class Binding {
        public int index;
        public AttribInfo pointer;
        public VertexBuffer vbo;
    }

    private int handle;
    private List<Binding> bindings;

    private IndexGenerator generator;
    private int maxElementCount;
    private IndexBuffer indexBuffer;

    public VertexArray() {
        bindings = new ArrayList<>();
        handle = glGenVertexArrays();
        bind();
        unbind();
    }

    public void setMaxElementCount(int maxElementCount) {
    }

    public void destroy() {
        glDeleteVertexArrays(handle);
    }

    public void bind() {
        glBindVertexArray(handle);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void draw(int count) {

    }

    public void enable(AttribInfo pointer, VertexBuffer buffer) {

    }
}