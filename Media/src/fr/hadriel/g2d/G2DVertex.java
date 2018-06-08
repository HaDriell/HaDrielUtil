package fr.hadriel.g2d;

import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.GLType;
import fr.hadriel.opengl.VertexAttribute;

public final class G2DVertex {
    public static final VertexAttribute[] VERTEX_LAYOUT = {
            new VertexAttribute("position", GLType.FLOAT, 2),
            new VertexAttribute("color", GLType.FLOAT, 4),
            new VertexAttribute("uv", GLType.FLOAT, 2)
    };


    public final Vec2 position;
    public final Vec4 color;
    public final Vec2 uv;

    public G2DVertex(Vec2 position, Vec4 color, Vec2 uv) {
        this.position = position;
        this.color = color;
        this.uv = uv;
    }
}