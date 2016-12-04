package fr.hadriel.hgl.graphics;


import fr.hadriel.hgl.opengl.AttribPointer;
import fr.hadriel.hgl.opengl.VertexBuffer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
/**
 * Created by HaDriel on 03/12/2016.
 */
public class Mesh {

    private int vao;
    private int vertexCount;
    private VertexBuffer[] vbos;

    public Mesh(int vertexCount, AttribPointer... attribPointers) {
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
                    pointer.count(),
                    pointer.type(),
                    pointer.normalized(),
                    0,
                    0);
            vbos[i] = vbo;
        }
        glBindVertexArray(0);
    }

    public VertexBuffer getVertexAttribBuffer(int index) {
        return vbos[index];
    }

    public void render() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
    }
}