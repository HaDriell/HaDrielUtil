package fr.hadriel.graphics.renderers;

import fr.hadriel.graphics.opengl.IndexBuffer;
import fr.hadriel.graphics.opengl.Shader;
import fr.hadriel.graphics.opengl.VertexArray;

import static org.lwjgl.opengl.GL11.*;

public class OpenGLRenderer {

    public void draw(Shader program, VertexArray vao, IndexBuffer ibo) {
        program.bind();
        vao.bind();
        ibo.bind();
        glDrawElements(GL_TRIANGLES, ibo.count(), ibo.getType().name, 0);
    }
}