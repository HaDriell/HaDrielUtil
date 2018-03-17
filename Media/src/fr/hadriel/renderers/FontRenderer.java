package fr.hadriel.renderers;

import fr.hadriel.asset.font.Font;
import fr.hadriel.asset.font.FontChar;
import fr.hadriel.graphics.image.Sprite;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;

import java.util.Arrays;

import static fr.hadriel.renderers.RenderUtil.*;

public class FontRenderer {
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
    private VertexBuffer vertexBuffer;

    public FontRenderer() {
        //init Shader
        this.shader = Shader.GLSL(FontRenderer.class.getResourceAsStream("font_shader.glsl"));
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

    public void draw(String text, Font font, float size, Vec4 color) {
        draw(Matrix3.Identity, text, font, size, color);
    }

    public void draw(Matrix3 transform, String text, Font font, float size, Vec4 color) {
        if(text == null || text.isEmpty()) return; // no text to render
        TextureSampler2D sampler2D = new TextureSampler2D(32); // same size of the u_page[n] sampler2D array

        float scale = size / font.info().size; // ratio between render size and desired size
        float advance = 0;


        vertexBuffer.bind().map();
        char previousCharacter = 0;
        for(char character : text.toCharArray()) {
            FontChar fc = font.character(character);
            //Char dimensions
            Vec2 fcPosition = new Vec2(fc.xoffset + advance, fc.yoffset).scale(scale);
            Vec2 fcSize = new Vec2(fc.width, fc.height).scale(scale);

            advance += fc.xadvance + font.kerning(previousCharacter, character);
            previousCharacter = character;

            //Texture info
            Sprite sprite = font.sprite(fc);
            if(sprite == null)
                continue;


            int texture = sampler2D.activateTexture(sprite.texture);

            //No texture mean no render in FontRenderer
            if(texture == -1)
                continue;

            //Draw a Quad
            vertexBuffer.write(transform.multiply(fcPosition.x, fcPosition.y))
                    .write(color)
                    .write(sprite.uv0)
                    .write(texture);
            vertexBuffer.write(transform.multiply(fcPosition.x + fcSize.x, fcPosition.y))
                    .write(color)
                    .write(sprite.uv1)
                    .write(texture);
            vertexBuffer.write(transform.multiply(fcPosition.x + fcSize.x, fcPosition.y + fcSize.y))
                    .write(color)
                    .write(sprite.uv2)
                    .write(texture);
            vertexBuffer.write(transform.multiply(fcPosition.x, fcPosition.y + fcSize.y))
                    .write(color)
                    .write(sprite.uv3)
                    .write(texture);
        }
        vertexBuffer.bind().unmap();

        sampler2D.bindTextures();
        shader.uniform("u_page[0]", sampler2D.getUniformValue());
        DrawTriangles(shader, vao, indexBuffer, state, text.length() * 6); // 2 triangles per character => 6 elements
    }
}