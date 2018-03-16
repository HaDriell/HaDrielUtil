package fr.hadriel.renderers;

import fr.hadriel.opengl.IndexBuffer;
import fr.hadriel.opengl.RenderState;
import fr.hadriel.opengl.VertexArray;
import fr.hadriel.opengl.shader.Shader;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {
    private RenderUtil() {}

    public static void Clear() {
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static void DrawTriangles(Shader shader, VertexArray vertexArray, int elementCount) {
        DrawTriangles(shader, vertexArray, null, null, elementCount);
    }

    public static void DrawTriangles(Shader shader, VertexArray vertexArray, RenderState state, int elementCount) {
        DrawTriangles(shader, vertexArray, null, state, elementCount);
    }

    public static void DrawTriangles(Shader shader, VertexArray vertexArray, IndexBuffer indexBuffer, int elementCount) {
        DrawTriangles(shader, vertexArray, indexBuffer, null, elementCount);
    }

    public static void DrawTriangles(Shader shader, VertexArray vertexArray, IndexBuffer indexBuffer, RenderState state, int elementCount) {
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
            glDrawElements(GL_TRIANGLES, elementCount, indexBuffer.getType().name, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, elementCount);
        }
    }
}
