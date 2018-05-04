package fr.hadriel.renderers;

import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.font.FontChar;
import fr.hadriel.asset.graphics.image.ImageRegion;
import fr.hadriel.math.*;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;

import static fr.hadriel.renderers.RenderUtil.DrawTriangles;

public class BatchRenderer {
    private static final int MODE_SPRITE = 0;
    private static final int MODE_SDF    = 1;


    private static final int MAX_SPRITES  = 100_000;
    private static final int MAX_VERTICES = MAX_SPRITES * 4;
    private static final int MAX_INDICES  = MAX_SPRITES * 6;

    private static final VertexAttribute[] UI_SHADER_LAYOUT = {
            new VertexAttribute("i_position", GLType.FLOAT, 2),
            new VertexAttribute("i_color", GLType.FLOAT, 4),
            new VertexAttribute("i_uv", GLType.FLOAT, 2),
            new VertexAttribute("i_texture", GLType.INT, 1),
            new VertexAttribute("i_mode", GLType.INT, 1)
    };

    private Shader shader;
    private RenderState state;
    private VertexArray vao;
    private IndexBuffer indexBuffer;


    //Batch Context
    private VertexBuffer vertexBuffer;
    private final TextureSampler2D sampler2D;
    private int elementCount;


    public BatchRenderer() {
        //init Shader
        this.shader = Shader.GLSL(BatchRenderer.class.getResourceAsStream("batch_shader.glsl"));
        this.sampler2D = new TextureSampler2D(32);
        shader.uniform("u_texture[0]", sampler2D.getUniformValue());
        //init VAO
        this.vao = new VertexArray(MAX_VERTICES, UI_SHADER_LAYOUT);
        this.vertexBuffer = vao.getBuffer();

        //init Indices
        this.indexBuffer = new IndexBuffer(MAX_INDICES, GLType.UINT);
        indexBuffer.bind().map();
        int i = 0;
        for(int quad = 0; quad < MAX_SPRITES; quad++) {
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

    public void setFontBuffer(float buffer) {
        shader.uniform("u_buffer", buffer);
    }

    public void setFontGamma(float gamma) {
        shader.uniform("u_gamma", gamma);
    }

    public void begin() {
        sampler2D.clear();
        elementCount = 0;
        vertexBuffer.bind().map();
    }

    private void submit(Matrix3 transform, float x, float y, float width, float height, Vec4 color,
                        Texture2D texture2D, int mode, Vec2 uv0, Vec2 uv1, Vec2 uv2, Vec2 uv3) {
        //Check Buffer capacity
        if (elementCount + 6 > MAX_SPRITES) {
            end();
            begin();
        }

        //Check Texture buffer capacity
        int texture = -1;
        if (texture2D != null) {
            texture = sampler2D.getActiveTextureIndex(texture2D);
            if (texture == -1) {
                if (sampler2D.isFull()) {
                    end();
                    begin();
                }
                texture = sampler2D.activateTexture(texture2D);
            }
        }

        //Draw the Quad
        vertexBuffer.write(transform.multiply(x, y))
                .write(color)
                .write(uv0)
                .write(texture)
                .write(mode);
        vertexBuffer.write(transform.multiply(x + width, y))
                .write(color)
                .write(uv1)
                .write(texture)
                .write(mode);
        vertexBuffer.write(transform.multiply(x + width, y + height))
                .write(color)
                .write(uv2)
                .write(texture)
                .write(mode);
        vertexBuffer.write(transform.multiply(x, y + height))
                .write(color)
                .write(uv3)
                .write(texture)
                .write(mode);

        //Consume elements (1 submit = 2 triangles = 6 elements)
        elementCount += 6;
    }


    public void draw(Matrix3 transform, float x, float y, String text, Font font, float size, Vec4 color) {
        if (text.isEmpty()) return; // no text to render

        float scale = size / font.info().size; // ratio between render size and desired size
        float xadvance = 0; // unscaled advance of the text cursor
        char previousCharacter = 0;
        for(char character : text.toCharArray()) {
            FontChar fc = font.character(character);
            //Char dimensions
            Vec2 fcPosition = new Vec2(xadvance + fc.xoffset, fc.yoffset);
            Vec2 fcSize = new Vec2(fc.width, fc.height);

            xadvance += fc.width - fc.xoffset + font.kerning(previousCharacter, character);
            previousCharacter = character;

            //Texture info
            ImageRegion region = font.sprite(fc);
            if (region == null)
                continue; // skip submit

            submit(transform, x + fcPosition.x * scale, y + fcPosition.y * scale, fcSize.x * scale, fcSize.y * scale, color, region.texture, MODE_SDF, region.uv0, region.uv1, region.uv2, region.uv3);
        }
    }

    public void draw(Matrix3 transform, float x, float y, float width, float height, ImageRegion region, Vec4 color) {
        submit(transform, x, y, width, height, color, region.texture, MODE_SPRITE, region.uv0, region.uv1, region.uv2, region.uv3);
    }

    public void draw(Matrix3 transform, float x, float y, float width, float height, Vec4 color) {
        submit(transform, x, y, width, height, color, null, MODE_SPRITE, Vec2.ZERO, Vec2.ZERO, Vec2.ZERO, Vec2.ZERO);
    }

    public void end() {
        vertexBuffer.bind().unmap();
        sampler2D.bindTextures();
        DrawTriangles(shader, vao, indexBuffer, state, elementCount);
    }
}