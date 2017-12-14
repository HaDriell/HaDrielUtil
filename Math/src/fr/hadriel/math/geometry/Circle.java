package fr.hadriel.math.geometry;

import fr.hadriel.math.Vec2;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class Circle extends Polygon {

    private static final int DEFAULT_VERTEX_COUNT = 24;
    private static final float DEFAULT_RADIUS = 1f;

    public Circle(int vertexCount, float radius) {
        super(CreatePolygonCircle(vertexCount, radius));
    }

    public static Vec2[] CreatePolygonCircle(int vertexCount, float radius) {
        Vec2 primitive = new Vec2(radius, 0);
        Vec2[] vertices = new Vec2[vertexCount];
        for(int i = 0; i < vertexCount; i++) {
            vertices[i] = primitive.rotate((360f * i) / vertexCount);
            System.out.println(vertices[i]);
        }
        return vertices;
    }
}