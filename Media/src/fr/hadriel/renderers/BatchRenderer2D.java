package fr.hadriel.renderers;

import fr.hadriel.opengl.Texture2D;
import fr.hadriel.opengl.TextureSampler;
import fr.hadriel.opengl.*;
import fr.hadriel.math.Matrix4;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.shader.Shader;
import org.lwjgl.opengl.GL11;


/**
 *
 * Created by HaDriel on 08/12/2016.
 */
public class BatchRenderer2D {

    private static final VertexAttribute[] BATCH_SHADER_LAYOUT = {
            new VertexAttribute("position", GLType.FLOAT, 2),
            new VertexAttribute("color", GLType.FLOAT, 4),
            new VertexAttribute("uv", GLType.FLOAT, 2),
            new VertexAttribute("tid", GLType.FLOAT, 1),
    };
    private static final int MAX_ELEMENT_COUNT = 100_000;

    private Matrix4 projection; // getProjection uniform

    private RenderState renderState;
    private Shader shader;
    private TextureSampler sampler;
    private IndexBuffer ibo;
    private VertexArray vao;
    private VertexBuffer vbo;
    private int elementCount;

    public BatchRenderer2D(float left, float right, float top, float bottom) {
        this.projection = Matrix4.Orthographic(left, right, top, bottom, -1, 1);
        this.shader = Shader.GLSL(getClass().getResourceAsStream("batch_shader.glsl"));
        this.vao = new VertexArray(MAX_ELEMENT_COUNT, BATCH_SHADER_LAYOUT);
        this.vbo = vao.getBuffer();
        this.ibo = new IndexBuffer(MAX_ELEMENT_COUNT, GLType.UINT);
        this.sampler = new TextureSampler();
        this.renderState = new RenderState();

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

    public void vertex(float px, float py, Vec4 color, float u, float v, Texture2D texture2D) {
        if(elementCount >= MAX_ELEMENT_COUNT || sampler.getTextureCount() > 31) {
            end(); // end the draw call to reset the texture2D binding
            begin();
        }
        vbo.write(px).write(py); // position
        vbo.write(color != null ? color : new Vec4(1, 1, 1, 1)); // color
        vbo.write(u).write(v); // uv
        vbo.write(texture2D != null ? sampler.load(texture2D) : -1f); // tid
        elementCount++;
    }

    public void end() {
        ibo.bind().unmap(); // apply the index buffer data
        vbo.bind().unmap(); // apply the vertex buffer data

        //Render
        renderState.apply();
        shader.bind();
        sampler.bindTextures();
        shader.uniform("texture2D", sampler.getSamplerTextureUnitsIndices());
        shader.uniform("getProjection", projection);
        vao.bind();
        ibo.bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, elementCount, ibo.getType().name, 0);
    }
}