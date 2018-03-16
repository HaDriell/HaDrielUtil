package fr.hadriel.renderers;

import fr.hadriel.graphics.font.Font;
import fr.hadriel.graphics.font.FontChar;
import fr.hadriel.graphics.image.Sprite;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;

import static fr.hadriel.renderers.RenderUtil.*;

public class FontRenderer {
    private static final int MAX_CHARACTERS = 10_000;
    private static final int MAX_ELEMENTS = MAX_CHARACTERS * 4;

    private static final VertexAttribute[] FONT_SHADER_LAYOUT = {
            new VertexAttribute("i_position", GLType.FLOAT, 2),
            new VertexAttribute("i_color", GLType.FLOAT, 4),
            new VertexAttribute("i_uv", GLType.FLOAT, 2),
            new VertexAttribute("i_texture", GLType.UINT, 1)
    };

    private Shader shader;
    private RenderState state;
    private VertexArray vao;

    public FontRenderer() {
        this.shader = Shader.GLSL(FontRenderer.class.getResourceAsStream("font_shader.glsl"));
        this.vao = new VertexArray(MAX_ELEMENTS, FONT_SHADER_LAYOUT);
        this.state = new RenderState();
        state.setBlending(true);
        state.setSrcBlendFactor(BlendFactor.GL_SRC_ALPHA);
        state.setDstBlendFactor(BlendFactor.GL_ONE_MINUS_SRC_ALPHA);
        state.setWindingClockwise();
    }

    public void setProjection(float left, float right, float top, float bottom) {
        shader.uniform("u_projection", Matrix4f.Orthographic(left, right, top, bottom, -1, 1));
    }

    public void setSmoothing(float smoothing) {
        shader.uniform("u_smoothing", smoothing);
    }

    public void draw(Matrix3 transform, String text, Font font, float size, Vec4 color) {
        if(text == null || text.isEmpty()) return; // no text to render
        TextureSampler2D sampler2D = new TextureSampler2D(32); // same size of the u_page[n] sampler2D array

        float scale = size / font.info().size; // ratio between render size and desired size
        float advance = 0;

        VertexBuffer vertexBuffer = vao.getBuffer();
        vertexBuffer.bind().map();

        char previousCharacter = 0;
        for(char character : text.toCharArray()) {
            FontChar fc = font.character(character);
            //Char dimensions
            Vec2 fcPosition = new Vec2(fc.xoffset + advance, fc.yoffset).scale(scale);
            Vec2 fcSize = new Vec2(fc.width, fc.height).scale(scale);

            //TODO : support kerning
            advance += fc.xadvance + font.kerning(character, previousCharacter);
            previousCharacter = character;

            //Texture info
            Sprite sprite = font.sprite(fc);
            if(sprite == null)
                continue;

            int texture = sampler2D.activateTexture(sprite.texture);

            //No texture mean no render in FontRenderer
            if(texture == -1)
                continue;

            //Vertex registration
            //Triangle 1
            Vec2 position = transform.multiply(fcPosition.x, fcPosition.y);
            vertexBuffer.write(position).write(color).write(sprite.uv0).write(texture);
            position = transform.multiply(fcPosition.x + fcSize.x, fcPosition.y);
            vertexBuffer.write(position).write(color).write(sprite.uv1).write(texture);
            position = transform.multiply(fcPosition.x + fcSize.x, fcPosition.y + fcSize.y);
            vertexBuffer.write(position).write(color).write(sprite.uv2).write(texture);
            //Triangle 2
            vertexBuffer.write(position).write(color).write(sprite.uv2).write(texture);
            position = transform.multiply(fcPosition.x, fcPosition.y + fcSize.y);
            vertexBuffer.write(position).write(color).write(sprite.uv3).write(texture);
            position = transform.multiply(fcPosition.x, fcPosition.y);
            vertexBuffer.write(position).write(color).write(sprite.uv0).write(texture);
        }
        vertexBuffer.unmap().unbind();

        sampler2D.bindTextures();
        shader.uniform("u_page[0]", sampler2D.getUniformValue());
        DrawTriangles(shader, vao, state, text.length() * 6); // 2 triangles per character => 6 vertices
    }
}