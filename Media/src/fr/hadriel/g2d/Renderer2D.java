package fr.hadriel.g2d;

import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;


public abstract class Renderer2D {

    public static final VertexAttribute[] VERTEX_LAYOUT = {
            new VertexAttribute("position", GLType.FLOAT, 2),
            new VertexAttribute("color", GLType.FLOAT, 4),
            new VertexAttribute("uv", GLType.FLOAT, 2)
    };

    private CommandBuffer commandBuffer;
    private VertexArray vertexArray;

    //Sprite context
    private int elementCount;

    protected Renderer2D(int maxVertexCount) {
        this.vertexArray = new VertexArray(maxVertexCount, VERTEX_LAYOUT);
    }

    //Staging functions
    public void begin() {
        commandBuffer.clear();
    }

    public void submit(Matrix3 transform, Sprite sprite, Vec4 color, Shader shader) {

    }

    public void end() {

    }
}