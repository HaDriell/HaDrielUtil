package fr.hadriel.renderers;

import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;

import static org.lwjgl.opengl.GL11.*;

public abstract class Mesh {

    private RenderState state;
    private Shader shader;
    private VertexArray vertexArray;
    private IndexBuffer indexBuffer;

    public void drawTriangles() {
        if(indexBuffer != null)
            drawTriangles(indexBuffer.count());
        else
            drawTriangles(vertexArray.getMaxElementCount());
    }

    public void drawTriangles(int elementCount) {
        if(vertexArray == null) return; // no data to render

        //Prepare Shader
        if(shader != null) {
            shader.bind();
            setUniforms(shader);
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

    protected abstract void setUniforms(Shader shader);
}