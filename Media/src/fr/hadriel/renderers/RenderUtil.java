package fr.hadriel.renderers;

import fr.hadriel.opengl.IndexBuffer;
import fr.hadriel.opengl.RenderState;
import fr.hadriel.opengl.VertexArray;
import fr.hadriel.opengl.shader.Shader;

import static org.lwjgl.opengl.GL11.*;

public final class RenderUtil {
    private RenderUtil() {}

    public static void Clear() {
        Clear(0, 0, 0, 1);
    }

    public static void Clear(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static void DrawTriangles(Shader shader, VertexArray vertexArray, int elementCount) {
        Draw(GL_TRIANGLES, shader, vertexArray, null, null, elementCount);
    }

    public static void DrawTriangles(Shader shader, VertexArray vertexArray, RenderState state, int elementCount) {
        Draw(GL_TRIANGLES, shader, vertexArray, null, state, elementCount);
    }

    public static void DrawTriangles(Shader shader, VertexArray vertexArray, IndexBuffer indexBuffer, int elementCount) {
        Draw(GL_TRIANGLES, shader, vertexArray, indexBuffer, null, elementCount);
    }

    public static void DrawTriangles(Shader shader, VertexArray vertexArray, IndexBuffer indexBuffer, RenderState state, int elementCount) {
        Draw(GL_TRIANGLES, shader, vertexArray, indexBuffer, state, elementCount);
    }

    public static void DrawTriangleStrip(Shader shader, VertexArray vertexArray, int elementCount) {
        Draw(GL_TRIANGLE_STRIP, shader, vertexArray, null, null, elementCount);
    }

    public static void DrawTriangleStrip(Shader shader, VertexArray vertexArray, RenderState state, int elementCount) {
        Draw(GL_TRIANGLE_STRIP, shader, vertexArray, null, state, elementCount);
    }

    public static void DrawTriangleStrip(Shader shader, VertexArray vertexArray, IndexBuffer indexBuffer, int elementCount) {
        Draw(GL_TRIANGLE_STRIP, shader, vertexArray, indexBuffer, null, elementCount);
    }

    public static void DrawTriangleStrip(Shader shader, VertexArray vertexArray, IndexBuffer indexBuffer, RenderState state, int elementCount) {
        Draw(GL_TRIANGLE_STRIP, shader, vertexArray, indexBuffer, state, elementCount);
    }

    public static void Draw(int glPrimitive, Shader shader, VertexArray vertexArray, IndexBuffer indexBuffer, RenderState state, int elementCount) {
        if(vertexArray == null) return; // no data to render

        //Prepare Shader
        if(shader != null) {
            shader.bind();
        }

        //Prepare Pipeline if provided
        if(state != null) state.apply();

        //Bind data (in case it was unbound)
        vertexArray.bind();

        //Choose between indexed and non indexed modes
        if(indexBuffer != null) {
            indexBuffer.bind();
            glDrawElements(glPrimitive, elementCount, indexBuffer.getType().name, 0);
        } else {
            glDrawArrays(glPrimitive, 0, elementCount);
        }
    }
}
