package fr.hadriel.math.geometry;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geometry.Epsilon;
import fr.hadriel.math.geometry.Geometry;

/**
 *
 * @author glathuiliere
 */
public class Polygon {

    public final Vec2[] vertices;
    public final Vec2[] normals;

    public Polygon(Vec2[] vertices, Vec2[] normals) {
        this.vertices = vertices;
        this.normals = normals;
    }

    public Polygon(Vec2... vertices) {
        validate(vertices);
        this.vertices = vertices;
        this.normals = Geometry.getCounterClockwiseEdgeNormals(vertices);
    }

    private static void validate(Vec2... vertices) {
        if (vertices == null) throw new NullPointerException("geometry.polygon.nullArray");
        int size = vertices.length;
        if (size < 3) throw new IllegalArgumentException("geometry.polygon.lessThan3Vertices");
        for (int i = 0; i < size; i++) {
            if (vertices[i] == null) throw new NullPointerException("geometry.polygon.nullVertices");
        }
        float area = 0f;
        float sign = 0f;
        for (int i = 0; i < size; i++) {
            Vec2 p0 = (i - 1 < 0) ? vertices[size - 1] : vertices[i - 1];
            Vec2 p1 = vertices[i];
            Vec2 p2 = (i + 1 == size) ? vertices[0] : vertices[i + 1];
            if (p1.equals(p2))
                throw new IllegalArgumentException("geometry.polygon.coincidentVertices");
            float cross = p0.to(p1).cross(p1.to(p2));
            float tsign = Math.signum(cross);
            area += cross;
            if (Mathf.abs(cross) > Epsilon.E) {
                if (sign != 0.0 && tsign != sign)
                    throw new IllegalArgumentException("geometry.polygon.nonConvex");
            }
            sign = tsign;
        }
        if (area < 0f)
            throw new IllegalArgumentException("geometry.polygon.invalidWinding");
    }
}