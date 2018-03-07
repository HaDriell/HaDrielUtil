package fr.hadriel.renderers;

import fr.hadriel.opengl.IndexBuffer;
import fr.hadriel.opengl.RenderState;
import fr.hadriel.opengl.Shader;
import fr.hadriel.opengl.VertexArray;

import static org.lwjgl.opengl.GL11.*;

public interface IRenderer<Graphics> {

    public void draw(Graphics object);

    public static void DrawTriangles(RenderState state, Shader shader, VertexArray vao) {
        DrawTriangles(state, shader, vao, vao.getMaxElementCount());
    }

    public static void DrawTriangles(RenderState state, Shader shader, VertexArray vao, int elementCount) {
        state.apply();
        shader.bind();
        vao.bind();
        glDrawArrays(GL_TRIANGLES, 0, elementCount);
    }

    public static void DrawTriangles(RenderState state, Shader shader, VertexArray vao, IndexBuffer ibo) {
        DrawTriangles(state, shader, vao, ibo, ibo.count());
    }

    public static void DrawTriangles(RenderState state, Shader shader, VertexArray vao, IndexBuffer ibo, int elementCount) {
        state.apply();
        shader.bind();
        vao.bind();
        ibo.bind();
        glDrawElements(GL_TRIANGLES, elementCount, ibo.getType().name, 0);
    }
}