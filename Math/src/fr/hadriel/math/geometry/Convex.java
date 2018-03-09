package fr.hadriel.math.geometry;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;

public class Convex extends Polygon {

    public Convex(Vec2... vertices) {
        super(vertices);
        validate(vertices);
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


    public List<Triangle> triangulate() {
        List<Triangle> result = new ArrayList<>(vertices.length - 2);
        int i = 0;
        Vec2 a = vertices[i++]; // i = 0
        Vec2 b = vertices[i++]; // i = 1
        do {
            Vec2 c = vertices[i++]; // next i
            result.add(new Triangle(a, b, c));
            b = c;
        } while (i < vertices.length);

        return result;
    }
}
