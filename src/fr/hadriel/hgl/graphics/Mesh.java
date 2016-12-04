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
    private Shader shader;

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
            glVertexAttribPointer(i,
                    pointer.size(),
                    pointer.type(),
                    pointer.normalized(),
                    0,
                    0);

            vbos[i] = vbo;
        }
        glBindVertexArray(0);
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void render() {
        if(shader == null) return; //skip rendering, Mesh is not renderable
        shader.bind();
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
        shader.unbind();
    }
}