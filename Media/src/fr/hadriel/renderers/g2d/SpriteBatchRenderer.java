package fr.hadriel.renderers.g2d;

import fr.hadriel.asset.graphics.image.ImageRegion;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;

public class SpriteBatchRenderer extends G2DBatchRenderer<ImageRegion> {
    public static final int DEFAULT_CAPACITY = 600_000;

    private final TextureSampler2D sampler2D;

    public SpriteBatchRenderer() {
        this(DEFAULT_CAPACITY);
    }

    public SpriteBatchRenderer(int vertexCapacity) {
        super(Shader.GLSL(SpriteBatchRenderer.class.getResourceAsStream("sprite_shader.glsl")),
                new VertexAttribute[] {
                        new VertexAttribute("position", GLType.FLOAT, 2),
                        new VertexAttribute("color", GLType.FLOAT, 4),
                        new VertexAttribute("uv", GLType.FLOAT, 2),
                        new VertexAttribute("texture", GLType.INT, 1),
                },
                vertexCapacity);
        this.sampler2D = new TextureSampler2D(32);
        shader.uniform("u_texture[0]", sampler2D.getUniformValue());
    }

    public void setProjection(float left, float right, float top, float bottom, float near, float far) {
        shader.uniform("u_projection", Matrix4.Orthographic(left, right, top, bottom, near, far));
    }


    protected boolean shouldFlush(ImageRegion imageRegion, int remainingVertices) {
        boolean samplerOverflow = sampler2D.isFull() && sampler2D.getActiveTextureIndex(imageRegion.texture) == -1;
        boolean vertexOverflow = remainingVertices < 6;
        return samplerOverflow || vertexOverflow;
    }

    protected void onBegin() {
        sampler2D.clear();
    }

    protected int onDraw(VertexBuffer vertexBuffer, Matrix3 transform, float depth, ImageRegion imageRegion) {
        int tid = sampler2D.activateTexture(imageRegion.texture);
        Vec4 color = new Vec4(1, 1, 1, 1);
        Vec2 p0 = transform.multiply(0, 0);
        Vec2 p1 = transform.multiply(imageRegion.texture.width, 0);
        Vec2 p2 = transform.multiply(imageRegion.texture.width, imageRegion.texture.height);
        Vec2 p3 = transform.multiply(0, imageRegion.texture.height);

        //Triangle 1
        vertexBuffer.write(p0).write(color).write(imageRegion.uv0).write(tid);
        vertexBuffer.write(p1).write(color).write(imageRegion.uv1).write(tid);
        vertexBuffer.write(p2).write(color).write(imageRegion.uv2).write(tid);

        //Triangle 2
        vertexBuffer.write(p2).write(color).write(imageRegion.uv2).write(tid);
        vertexBuffer.write(p3).write(color).write(imageRegion.uv3).write(tid);
        vertexBuffer.write(p0).write(color).write(imageRegion.uv0).write(tid);
        return 6;
    }

    protected void onEnd() {
        sampler2D.bindTextures();
    }
}