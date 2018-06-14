package fr.hadriel.g2d;

import fr.hadriel.opengl.Framebuffer;
import fr.hadriel.opengl.VertexArray;
import fr.hadriel.opengl.shader.Shader;

import static org.lwjgl.opengl.GL11.*;

public class G2DRenderer {
    public static final String G2D_PROJECTION = "g2d_projection";
    public static final String G2D_MODEL = "g2d_model";
    public static final String G2D_VIEW = "g2d_view";

    public void render(G2DScene scene, G2DCamera camera, Framebuffer framebuffer) {
        if (framebuffer != null)
            framebuffer.bind();
        else
            Framebuffer.BindDefault();

        for(G2DShaderPass pass : scene) {
            Shader shader = scene.getShader(pass.getShaderID());
            if (shader == null) { // TODO : log that ?
                continue; // skip, no shader to render
            }

            shader.bind();
            pass.setUniforms(shader); // in order to avoid external override, system uniforms are set AFTER user uniforms
            shader.setUniform(G2D_PROJECTION, camera.getProjectionTransform());
            shader.setUniform(G2D_VIEW, camera.getViewTransform());

            for(G2DInstance instance : pass) {
                G2DMesh mesh = scene.getMesh(instance.getMeshID());
                if (mesh == null) { // TODO : Log that ?
                    continue;
                }

                VertexArray vertexArray = mesh.getVertexArray();
                if (vertexArray == null) { // TODO : Log that ?
                    continue;
                }

                //Draw the Instance
                shader.setUniform(G2D_MODEL, instance.getTransform());
                vertexArray.bind();
                glDrawArrays(GL_TRIANGLES, 0, vertexArray.getMaxElementCount());
            }
        }
    }
}