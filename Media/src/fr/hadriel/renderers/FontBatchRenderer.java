package fr.hadriel.renderers;

import com.sun.istack.internal.NotNull;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.font.FontChar;
import fr.hadriel.asset.graphics.image.ImageRegion;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;

import static fr.hadriel.renderers.RenderUtil.*;

public class FontBatchRenderer {
    private static final int MAX_CHARACTERS = 100_000;
    private static final int MAX_VERTICES   = MAX_CHARACTERS * 4;
    private static final int MAX_INDICES    = MAX_CHARACTERS * 6;

    private static final VertexAttribute[] FONT_SHADER_LAYOUT = {
            new VertexAttribute("i_position", GLType.FLOAT, 2),
            new VertexAttribute("i_color", GLType.FLOAT, 4),
            new VertexAttribute("i_uv", GLType.FLOAT, 2),
            new VertexAttribute("i_texture", GLType.UINT, 1)
    };

    private Shader shader;
    private RenderState state;
    private VertexArray vao;
    private IndexBuffer indexBuffer;

    // Batch context
    private VertexBuffer vertexBuffer;
    private final TextureSampler2D sampler2D;
    private int elementCount;

    public FontBatchRenderer() {
        //init Shader
        this.shader = Shader.GLSL(FontBatchRenderer.class.getResourceAsStream("font_shader.glsl"));
        this.sampler2D = new TextureSampler2D(32);
        shader.uniform("u_page[0]", sampler2D.getUniformValue());
        setWeight(0.5f);
        setEdge(0.1f);

        //init VAO
        this.vao = new VertexArray(MAX_VERTICES, FONT_SHADER_LAYOUT);
        this.vertexBuffer = vao.getBuffer();

        //init Indices
        this.indexBuffer = new IndexBuffer(MAX_INDICES, GLType.UINT);
        indexBuffer.bind().map();
        int i = 0;
        for(int quad = 0; quad < MAX_CHARACTERS; quad++) {
            indexBuffer.write(i + 0, i + 1, i + 2); // Triangle 1
            indexBuffer.write(i + 2, i + 3, i + 0); // Triangle 2
            i += 4;
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

    public void setWeight(float weight) {
        shader.uniform("u_weight", weight);
    }

    public void setEdge(float edge) {
        shader.uniform("u_edge", edge);
    }

    public void begin() {
        sampler2D.clear();
        elementCount = 0;
        vertexBuffer.bind().map();
    }

    public void draw(Matrix3 transform, @NotNull String text, @NotNull Font font, float size, Vec4 color) {
        if(text.isEmpty()) return; // no text to render

        if (elementCount + 6 > MAX_CHARACTERS) {
            end();
            begin();
        }

        float scale = size / font.info().size; // ratio between render size and desired size
        float advance = 0; // unscaled advance of the text cursor
        char previousCharacter = 0;
        for(char character : text.toCharArray()) {
            FontChar fc = font.character(character);
            //Char dimensions
            Vec2 fcPosition = new Vec2(fc.xoffset + advance, fc.yoffset).scale(scale);
            Vec2 fcSize = new Vec2(fc.width, fc.height).scale(scale);

            advance += fc.xadvance + font.kerning(previousCharacter, character);
            previousCharacter = character;

            //Texture info
            ImageRegion region = font.sprite(fc);

            //Load texture in batch
            int texture = -1;
            if (region != null) {
                texture = sampler2D.getActiveTextureIndex(region.texture);
                if (texture == -1) {
                    if (sampler2D.isFull()) {
                        end();
                        begin();
                    }
                    texture = sampler2D.activateTexture(region.texture);
                }
            }

            vertexBuffer.write(transform.multiply(fcPosition.x, fcPosition.y))
                    .write(color)
                    .write(region == null ? Vec2.ZERO : region.uv0)
                    .write(texture);
            vertexBuffer.write(transform.multiply(fcPosition.x + fcSize.x, fcPosition.y))
                    .write(color)
                    .write(region == null ? Vec2.ZERO : region.uv1)
                    .write(texture);
            vertexBuffer.write(transform.multiply(fcPosition.x + fcSize.x, fcPosition.y + fcSize.y))
                    .write(color)
                    .write(region == null ? Vec2.ZERO : region.uv2)
                    .write(texture);
            vertexBuffer.write(transform.multiply(fcPosition.x, fcPosition.y + fcSize.y))
                    .write(color)
                    .write(region == null ? Vec2.ZERO : region.uv3)
                    .write(texture);

            elementCount += 6; // 6 indices consumed for 4 vertices
        }
    }

    public void end() {
        vertexBuffer.bind().unmap();
        sampler2D.bindTextures();
        DrawTriangles(shader, vao, indexBuffer, state, elementCount);
    }
}