package fr.hadriel.graphics.renderers;

import fr.hadriel.graphics.opengl.*;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec4;
import static org.lwjgl.opengl.GL11.*;


/**
 *
 * Created by HaDriel on 08/12/2016.
 */
public class BatchRenderer2D {

    private static final AttribPointer[] BATCH_SHADER_LAYOUT = {
            new AttribPointer("position", GLType.FLOAT, 2),
            new AttribPointer("color", GLType.FLOAT, 4),
            new AttribPointer("uv", GLType.FLOAT, 2),
            new AttribPointer("tid", GLType.FLOAT, 1),
    };
    private static final int MAX_ELEMENT_COUNT = 100_000;

    private Matrix4f projection; // projection uniform

    private FaceCulling faceCulling;
    private BlendMode blendMode;
    private Shader shader;
    private TextureSampler sampler;
    private IndexBuffer ibo;
    private SingleBufferVertexArray vao;
    private VertexBuffer vbo;
    private int elementCount;

    //IBO context
    private int indexOffset;
    private int indexCount;

    public BatchRenderer2D(float left, float right, float top, float bottom) {
        this.projection = Matrix4f.Orthographic(left, right, top, bottom, -1, 1);
        this.faceCulling = new FaceCulling();
        this.blendMode = new BlendMode();
        this.shader = Shader.GLSL(getClass().getResourceAsStream("batch_shader.glsl"));
        this.vao = new SingleBufferVertexArray(MAX_ELEMENT_COUNT, BATCH_SHADER_LAYOUT);
        this.vbo = vao.getBuffer(0);
        this.ibo = new IndexBuffer(MAX_ELEMENT_COUNT, GLType.UINT);
        this.sampler = new TextureSampler();
    }

    public FaceCulling getFaceCulling() {
        return faceCulling;
    }

    public BlendMode getBlendMode() {
        return blendMode;
    }

    public void begin() {
        vbo.bind().map(); // get access to the vertex buffer
        ibo.bind().map(); // get access to the index buffer
        sampler.reset(); // clear the sampler data
        indexOffset = elementCount = 0;
        indexCount = 0;
    }

    public void indices(int... indices) {
        for(int indice : indices) {
            ibo.write(indexOffset + indice);
        }
        indexOffset = elementCount;
        indexCount += indices.length;
    }

    public void indices(short... indices) {
        for(short indice : indices) {
            ibo.write((short) (indexOffset + indice));
        }
        indexOffset = elementCount;
        indexCount += indices.length;
    }

    public void indices(byte... indices) {
        for(byte indice : indices) {
            ibo.write((byte) (indexOffset + indice));
        }
        indexOffset = elementCount;
        indexCount += indices.length;
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
        ibo.bind().unmap(); // update the index buffer data
        vbo.bind().unmap(); // update the vertex buffer data

        //Render
        faceCulling.enable();
        blendMode.enable();
        shader.bind();
        sampler.bindTextures();
        shader.setUniform1iv("textures", sampler.getSamplerTextureUnitsIndices());
        shader.setUniformMat4f("projection", projection);

        vao.bind();
        ibo.bind();
        vao.drawElements(GL_TRIANGLES, indexCount, ibo.getIndexType().name);
        vao.unbind();
        ibo.unbind();
        shader.unbind();
        blendMode.disable();
        faceCulling.disable();
    }
}