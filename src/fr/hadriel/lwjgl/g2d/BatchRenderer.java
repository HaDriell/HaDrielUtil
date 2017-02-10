package fr.hadriel.lwjgl.g2d;

import fr.hadriel.lwjgl.opengl.*;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec4;
import static org.lwjgl.opengl.GL11.*;


/**
 * Created by HaDriel on 08/12/2016.
 */
public class BatchRenderer {

    private static final Vec4 WHITE = new Vec4(1, 1, 1, 1);
    private static final int MAX_QUADS = 60000 / 4;

    private Matrix4f projection;

    private BlendMode blendMode;
    private Shader shader;
    private TextureSampler sampler;
    private SingleBufferVertexArray vao;
    private VertexBuffer vbo;
    private int elementCount;

    public BatchRenderer(float left, float right, float top, float bottom) {
        this.projection = Matrix4f.Orthographic(left, right, top, bottom, -1, 1);
        this.blendMode = new BlendMode();

        this.shader = new Shader(
                getClass().getResourceAsStream("batch.vert"),
                getClass().getResourceAsStream("batch.frag")
        );

        this.vao = new SingleBufferVertexArray(MAX_QUADS * 4, // using GL_QUADS, so 4 vertices only
                new AttribPointer("position", GLType.FLOAT, 2),
                new AttribPointer("color", GLType.FLOAT, 4),
                new AttribPointer("uv", GLType.FLOAT, 2),
                new AttribPointer("tid", GLType.FLOAT, 1));

        this.vbo = vao.getBuffer(0);

        this.sampler = new TextureSampler();
    }

    public BlendMode getBlendMode() {
        return blendMode;
    }

    public void begin() {
        vbo.bind().map();
        sampler.reset();
        elementCount = 0;
    }

    public void submit(float px, float py) {
        submit(px, py, null, 0, 0, null);
    }

    public void submit(float px, float py, Vec4 color) {
        submit(px, py, color, 0, 0, null);
    }

    public void submit(float px, float py, Vec4 color, float u, float v, Texture texture) {
        if(elementCount >= MAX_QUADS || sampler.getTextureCount() > 31) {
            end();
            begin();
        }
        vbo.write(px).write(py); // position
        vbo.write(color != null ? color : WHITE); // color
        vbo.write(u).write(v); // uv
        vbo.write(texture != null ? sampler.load(texture) : -1f); // tid
        elementCount++;
    }

    public void end() {
        vbo.unmap().unbind();
        //Render
        blendMode.setup();
        shader.bind();
        sampler.bindTextures();
        shader.setUniform1iv("textures", sampler.getSamplerTextureUnitsIndices());
        shader.setUniformMat4f("projection", projection);
        vao.bind();
        vao.draw(GL_QUADS, elementCount); // triangulation will be done by driver (faster than Java ?)
        vao.unbind();
        shader.unbind();
    }
}