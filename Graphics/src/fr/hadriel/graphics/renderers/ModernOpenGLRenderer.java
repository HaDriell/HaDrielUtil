package fr.hadriel.graphics.renderers;

import fr.hadriel.graphics.opengl.BlendMode;
import fr.hadriel.graphics.opengl.Shader;
import fr.hadriel.graphics.opengl.VertexArray;
import fr.hadriel.math.Matrix4f;

import java.util.Objects;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public abstract class ModernOpenGLRenderer implements IRenderer {

    protected final Matrix4f projection;
    protected final Shader shader;
    protected final VertexArray vao;
    protected final BlendMode blendMode;

    protected ModernOpenGLRenderer(Shader shader, VertexArray vao) {
        this.shader = Objects.requireNonNull(shader);
        this.vao = Objects.requireNonNull(vao);
        this.blendMode = new BlendMode();
        this.projection = Matrix4f.Orthographic(-1, 1, 1, -1, -1, 1);
    }

    public BlendMode getBlendMode() {
        return blendMode;
    }

    public void begin() {

    }

    public void end() {

    }
}