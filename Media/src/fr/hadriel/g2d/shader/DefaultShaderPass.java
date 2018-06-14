package fr.hadriel.g2d.shader;

import fr.hadriel.g2d.G2DShaderPass;
import fr.hadriel.opengl.shader.Shader;

public class DefaultShaderPass extends G2DShaderPass {

    private static Shader shader;

    private static Shader getShader() {
        if (shader == null) {
            shader = Shader.GLSL(DefaultShaderPass.class.getResourceAsStream("g2d_shader.glsl"));
        }
        return shader;
    }

    protected void setUniforms(Shader shader) {
        shader.setUniform("u_texture", 0);
    }
}
