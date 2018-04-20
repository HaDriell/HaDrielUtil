package fr.hadriel.renderers;

import com.sun.istack.internal.NotNull;
import fr.hadriel.graphics.image.ImageRegion;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.opengl.*;
import fr.hadriel.math.Matrix4;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.shader.Shader;

import static fr.hadriel.renderers.RenderUtil.*;

/**
 *
 * Created by HaDriel on 08/12/2016.
 */
public class SpriteBatchRenderer {
    private static final int MAX_SPRITES  = 100_000;
    private static final int MAX_VERTICES = MAX_SPRITES * 4;
    private static final int MAX_INDICES  = MAX_SPRITES * 6;

    private static final VertexAttribute[] BATCH_SHADER_LAYOUT = {
            new VertexAttribute("i_position", GLType.FLOAT, 2),
            new VertexAttribute("i_color", GLType.FLOAT, 4),
            new VertexAttribute("i_uv", GLType.FLOAT, 2),
            new VertexAttribute("i_texture", GLType.INT, 1),
    };

    private Shader shader;
    private RenderState state;
    private VertexArray vao;
    private IndexBuffer indexBuffer;
    private VertexBuffer vertexBuffer;

    //Batch Context
    private final TextureSampler2D sampler2D;
    private int elementCount;

    public SpriteBatchRenderer() {
        //init Shader
        this.shader = Shader.GLSL(FontRenderer.class.getResourceAsStream("spritebatch_shader.glsl"));
        this.sampler2D = new TextureSampler2D(32); // same value of u_texture
        shader.uniform("u_texture[0]", sampler2D.getUniformValue());

        //init VAO
        this.vao = new VertexArray(MAX_VERTICES, BATCH_SHADER_LAYOUT);
        this.vertexBuffer = vao.getBuffer();

        this.indexBuffer = new IndexBuffer(MAX_INDICES, GLType.UINT);
        indexBuffer.bind().map();
        int quad = 0;
        for (int element = 0; element < MAX_SPRITES; element++) {
            indexBuffer.write(quad + 0, quad + 1, quad + 2); // Triangle 1
            indexBuffer.write(quad + 2, quad + 3, quad + 0); // Triangle 2
            quad += 4;
        }
        indexBuffer.unmap().unbind();

        //init RenderState
        this.state = new RenderState();
        state.setBlending(true);
        state.setSrcBlendFactor(BlendFactor.GL_SRC_ALPHA);
        state.setDstBlendFactor(BlendFactor.GL_ONE_MINUS_SRC_ALPHA);
        state.setBlendEquation(BlendEquation.GL_FUNC_ADD);
    }

    public void setProjection(float left, float right, float top, float bottom) {
        shader.uniform("u_projection", Matrix4.Orthographic(left, right, top, bottom, -1, 1));
    }

    public void begin() {
        sampler2D.clear();
        elementCount = 0;
        vertexBuffer.bind().map();
    }
    public void draw(@NotNull Matrix3 transform, float width, float height, ImageRegion region) { draw(transform, width, height, region, new Vec4(1,1,1,1)); }
    public void draw(@NotNull Matrix3 transform, float width, float height, ImageRegion region, @NotNull Vec4 color) {
        //Check the vertexBufferCapacity
        if (elementCount + 6 > MAX_VERTICES) {
            end();
            begin();
        }

        //Load texture in batch
        int texture = -1;
        if (region != null) {
            if (sampler2D.isFull()) {
                end();
                begin();
            }
            texture = sampler2D.activateTexture(region.texture);
        }

        vertexBuffer.write(transform.multiply(0, 0))
                .write(color)
                .write(region == null ? Vec2.ZERO : region.uv0)
                .write(texture);
        vertexBuffer.write(transform.multiply(width, 0))
                .write(color)
                .write(region == null ? Vec2.ZERO : region.uv1)
                .write(texture);
        vertexBuffer.write(transform.multiply(width, height))
                .write(color)
                .write(region == null ? Vec2.ZERO : region.uv2)
                .write(texture);
        vertexBuffer.write(transform.multiply(0, height))
                .write(color)
                .write(region == null ? Vec2.ZERO : region.uv3)
                .write(texture);

        vertexBuffer.debug(); // FIXME : something is clearly wrong with the putInt method. Find out what the fuck is going on.
        elementCount += 6; // 6 indices consumed for 4 vertices
    }

    public void end() {
        vertexBuffer.unmap();
        sampler2D.bindTextures();
        DrawTriangles(shader, vao, indexBuffer, state, elementCount);
    }
}