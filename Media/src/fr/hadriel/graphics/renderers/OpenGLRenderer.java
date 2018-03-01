package fr.hadriel.graphics.renderers;

import fr.hadriel.opengl.IndexBuffer;
import fr.hadriel.opengl.RenderState;
import fr.hadriel.opengl.Shader;
import fr.hadriel.opengl.VertexArray;

import static org.lwjgl.opengl.GL11.*;

public class OpenGLRenderer {

    protected final RenderState renderState = new RenderState();

    public final void draw(Shader program, VertexArray vao, IndexBuffer ibo) {
        renderState.apply();
        program.bind();
        vao.bind();
        ibo.bind();
        glDrawElements(GL_TRIANGLES, ibo.count(), ibo.getType().name, 0);
    }

    public RenderState getRenderState() {
        return renderState;
    }
}