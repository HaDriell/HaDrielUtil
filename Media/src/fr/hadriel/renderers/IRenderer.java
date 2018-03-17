package fr.hadriel.renderers;

import fr.hadriel.opengl.IndexBuffer;
import fr.hadriel.opengl.RenderState;
import fr.hadriel.opengl.VertexArray;
import fr.hadriel.opengl.shader.Shader;

import static org.lwjgl.opengl.GL11.*;
/**
 * Created by gauti on 02/03/2018.
 */
public interface IRenderer {

    /**
     * Setup shader uniforms
     */
    public void prepare();

    /**
     * Execute draw calls
     * */
    public void present();

    public static void DrawCall(Shader program, VertexArray vertexArray, IndexBuffer indexBuffer, RenderState renderState) {
        renderState.apply();
        program.bind();
        vertexArray.bind();
        indexBuffer.bind();
        glDrawElements(GL_TRIANGLES, indexBuffer.count(), indexBuffer.getType().name, 0);
    }
}