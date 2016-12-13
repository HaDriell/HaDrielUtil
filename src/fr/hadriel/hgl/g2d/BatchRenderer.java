package fr.hadriel.hgl.g2d;

import fr.hadriel.hgl.opengl.*;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec4;
import static org.lwjgl.opengl.GL11.*;


/**
 * Created by HaDriel setOn 08/12/2016.
 */
public class BatchRenderer {

    private static final Vec4 WHITE = new Vec4(1, 1, 1, 1);
    private static final int MAX_QUADS = 60000 / 4;

    private Matrix4f projection;

    private BlendMode blendMode;
    private Shader shader;
    private Texture2DSampler sampler;
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

        this.sampler = new Texture2DSampler();
    }

    public BlendMode getBlendMode() {
        return blendMode;
    }

    public void begin() {
        vbo.bind().map();
        sampler.reset();
        elementCount = 0;
    }

//    public void drawRect(Matrix4f matrix, float x, float y, float width, float height, float strokeWidth, Vec4 color) {
//        float dx = x + width;
//        float dy = y + height;
//        drawLine(matrix, x, y, dx, y, strokeWidth, color);
//        drawLine(matrix, dx, y, dx, dy, strokeWidth, color);
//        drawLine(matrix, dx, dy, x, dy, strokeWidth, color);
//        drawLine(matrix, x, dy, x, y, strokeWidth, color);
//    }

//    public void drawLine(Matrix4f matrix, Vec2 a, Vec2 b, float strokeWidth, Vec4 color) {
//        drawLine(matrix, a.x, a.y, b.x, b.y, strokeWidth, color);
//    }
//
//    public void drawTextureRegion(Matrix4f matrix, Vec2 position, Vec2 size, TextureRegion region) {
//        drawTextureRegion(matrix, position, size, region, null);
//    }
//
//    public void drawTextureRegion(Matrix4f matrix, float x, float y, float width, float height, TextureRegion region) {
//        drawTextureRegion(matrix, x, y, width, height, region, null);
//    }
//
//    public void drawTextureRegion(Matrix4f matrix, Vec2 position, Vec2 size, TextureRegion region, Vec4 color) {
//        drawTextureRegion(matrix, position.x, position.y, size.x, size.y, region, color);
//    }

//    public void drawLine(Matrix4f matrix, float xa, float ya, float xb, float yb, float strokeWidth, Vec4 color) {
//        float weight = strokeWidth / 2;
//        requireElements(4);
//        Vec2 line = new Vec2(xb - xa, yb - ya);
//        Vec2 heavyness = line.getNormalLeft().scale(weight, weight);
//        writeVertex(matrix, xa + heavyness.x, ya + heavyness.y, color);
//        writeVertex(matrix, xa - heavyness.x, ya - heavyness.y, color);
//        writeVertex(matrix, xb - heavyness.x, yb - heavyness.y, color);
//        writeVertex(matrix, xb + heavyness.x, yb + heavyness.y, color);
//    }

//    public void fillRect(Matrix4f matrix, float x, float y, float width, float height, Vec4 color) {
//        requireElements(4);
//        writeVertex(matrix, x, y, color);
//        writeVertex(matrix, x + width, y, color);
//        writeVertex(matrix, x + width, y + height, color);
//        writeVertex(matrix, x, y + height, color);
//    }

//    public void drawTextureRegion(Matrix4f matrix, float x, float y, float width, float height, TextureRegion region, Vec4 color) {
//        requireElements(4);
//        writeVertex(matrix, x, y, color, region.uv0.x, region.uv0.y, region.texture);
//        writeVertex(matrix, x + width, y, color, region.uv1.x, region.uv1.y, region.texture);
//        writeVertex(matrix, x + width, y + height, color, region.uv2.x, region.uv2.y, region.texture);
//        writeVertex(matrix, x, y + height, color, region.uv3.x, region.uv3.y, region.texture);
//    }

    public void submit(float px, float py) {
        submit(px, py, null, 0, 0, null);
    }

    public void submit(float px, float py, Vec4 color) {
        submit(px, py, color, 0, 0, null);
    }

    public void submit(float px, float py, Vec4 color, float u, float v, Texture2D texture) {
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