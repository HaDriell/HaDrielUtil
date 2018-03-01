package fr.hadriel.graphics.renderers;

import fr.hadriel.opengl.*;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec4;


/**
 *
 * Created by HaDriel on 08/12/2016.
 */
public class BatchRenderer2D extends OpenGLRenderer {

    private static final AttribPointer[] BATCH_SHADER_LAYOUT = {
            new AttribPointer("position", GLType.FLOAT, 2),
            new AttribPointer("color", GLType.FLOAT, 4),
            new AttribPointer("uv", GLType.FLOAT, 2),
            new AttribPointer("tid", GLType.FLOAT, 1),
    };
    private static final int MAX_ELEMENT_COUNT = 100_000;

    private Matrix4f projection; // projection uniform

    private Shader shader;
    private TextureSampler sampler;
    private IndexBuffer ibo;
    private SingleBufferVertexArray vao;
    private VertexBuffer vbo;
    private int elementCount;

    public BatchRenderer2D(float left, float right, float top, float bottom) {
        this.projection = Matrix4f.Orthographic(left, right, top, bottom, -1, 1);
        this.shader = Shader.GLSL(getClass().getResourceAsStream("batch_shader.glsl"));
        this.vao = new SingleBufferVertexArray(MAX_ELEMENT_COUNT, BATCH_SHADER_LAYOUT);
        this.vbo = vao.getBuffer(0);
        this.ibo = new IndexBuffer(MAX_ELEMENT_COUNT, GLType.UINT);
        this.sampler = new TextureSampler();

        //Face Culling
        renderState.setFaceCulling(false);
        //Blending Configuration
        renderState.setBlending(true);
        renderState.setSrcBlendFactor(BlendFactor.GL_SRC_ALPHA);
        renderState.setDstBlendFactor(BlendFactor.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void begin() {
        vbo.bind().map(); // get access to the vertex buffer
        ibo.bind().map(); // get access to the index buffer
        ibo.count(0);     // reset the index count to 0
        sampler.reset(); // clear the sampler data
    }

    public void indices(int... indices) {
        ibo.write(indices);
    }

    public void indices(short... indices) {
        ibo.write(indices);
    }

    public void indices(byte... indices) {
        ibo.write(indices);
    }

    public void vertex(float px, float py) {
        vertex(px, py, null, 0, 0, null);
    }

    public void vertex(float px, float py, Vec4 color) {
        vertex(px, py, color, 0, 0, null);
    }

    public void vertex(float px, float py, Vec4 color, float u, float v, Texture texture) {
        if(elementCount >= MAX_ELEMENT_COUNT || sampler.getTextureCount() > 31) {
            end(); // end the draw call to reset the texture binding
            begin();
        }
        vbo.write(px).write(py); // position
        vbo.write(color != null ? color : new Vec4(1, 1, 1, 1)); // color
        vbo.write(u).write(v); // uv
        vbo.write(texture != null ? sampler.load(texture) : -1f); // tid
        elementCount++;
    }

    public void end() {
        ibo.bind().unmap(); // apply the index buffer data
        vbo.bind().unmap(); // apply the vertex buffer data

        //Render
        shader.bind();
        sampler.bindTextures();
        shader.setUniform1iv("textures", sampler.getSamplerTextureUnitsIndices());
        shader.setUniformMat4f("projection", projection);
        draw(shader, vao, ibo);
    }
}