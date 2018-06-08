package fr.hadriel.g2d;

import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.opengl.shader.UniformBuffer;

import java.util.Objects;

public final class G2DMaterial {

    private Shader shader;
    private UniformBuffer uniforms;

    public G2DMaterial(Shader shader, UniformBuffer uniforms) {
        this.shader = Objects.requireNonNull(shader);
        this.uniforms = uniforms;
    }

    public void bind() {
        shader.bind();
        uniforms.setupUniforms(shader);
    }

    public void unbind() {
        shader.unbind();
    }
}