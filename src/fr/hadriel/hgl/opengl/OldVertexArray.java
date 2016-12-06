package fr.hadriel.hgl.opengl;


import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
/**
 * Created by HaDriel on 03/12/2016.
 */
public class OldVertexArray {

    private int vao;
    private int vertexCount;
    private VertexBuffer[] vbos;

    public OldVertexArray(int vertexCount, AttribPointer... attribPointers) {
        if(attribPointers.length == 0) throw new IllegalArgumentException("Cannot create Mesh without attrib pointers");
        this.vbos = new VertexBuffer[attribPointers.length];
        this.vertexCount = vertexCount;
        this.vao = glGenVertexArrays();

        //VBO binding
        glBindVertexArray(vao);
        for(int i = 0; i < attribPointers.length; i++) {
            AttribPointer pointer = attribPointers[i];
            VertexBuffer vbo = new VertexBuffer(pointer.getAttribSize() * vertexCount);
            vbo.bind();
            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i,
                    pointer.components(),
                    pointer.type(),
                    pointer.normalized(),
                    0,
                    0);
            vbos[i] = vbo;
        }
        glBindVertexArray(0);
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public VertexBuffer getBuffer(int index) {
        return vbos[index];
    }

    public void bind() {
        glBindVertexArray(vao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void draw(int mode) {
        glDrawArrays(mode, 0, vertexCount);
    }
}