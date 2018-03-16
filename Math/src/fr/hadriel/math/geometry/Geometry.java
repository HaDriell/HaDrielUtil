package fr.hadriel.math.geometry;

import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;

public strictfp final class Geometry {
    private Geometry() {}

    // Collision tests
    public static boolean isColliding(Circle a, Matrix3 tA, Circle b, Matrix3 tB) {
        Vec2 tACenter = tA.multiply(a.center);
        Vec2 tBCenter = tB.multiply(b.center);
        float distance2 = tACenter.distance2(tBCenter);
        float radius2 = (a.radius + b.radius) * (a.radius + b.radius);
        return distance2 < radius2;
    }

    public static Vec2 intersectLineLine(Vec2 a1, Vec2 a2, Vec2 b1, Vec2 b2) {
        Vec2 s1 = a1.sub(a2);
        Vec2 s2 = b1.sub(b2);
        float det = s1.cross(s2);

        if(Math.abs(det) <= Epsilon.E)
            return null;

        det = 1f / det;
        float t2 = det * (a1.cross(s1) - b1.cross(s1));
        return new Vec2(b1.x * (1f - t2) + b2.x * t2, b1.y * (1f - t2) + b2.y * t2);
    }

    // Intersection tests
    public static Vec2 intersectLineLine(float a1, float b1, float a2, float b2) {
        float d = (a1 - a2);
        if(d < Epsilon.E)
            return null;
        float x = (b2 - b1) / d;
        float y = a1 * x + b1;
        return new Vec2(x, y);
    }

    public static Vec2 intersectSegmentSegment(Vec2 a1, Vec2 a2, Vec2 b1, Vec2 b2) {
        Vec2 A = a1.to(a2);
        Vec2 B = b1.to(b2);

        // compute the bottom
        float BxA = B.cross(A);
        if (Math.abs(BxA) <= Epsilon.E) {
            // the lines are parallel and don't intersect
            return null;
        }

        // compute the top
        float ambxA = a1.sub(b1).cross(A);
        if (Math.abs(ambxA) <= Epsilon.E) {
            // the lines are coincident
            return null;
        }

        // compute tb
        float tb = ambxA / BxA;
        // compute the intersection point
        return B.scale(tb).add(b1);
    }

    public static final Vec2[] getCounterClockwiseEdgeNormals(Vec2... vertices) {
        if (vertices == null) return null;

        int size = vertices.length;
        if (size == 0) return null;

        Vec2[] normals = new Vec2[size];
        for (int i = 0; i < size; i++) {
            Vec2 p1 = vertices[i];
            Vec2 p2 = (i + 1 == size) ? vertices[0] : vertices[i + 1];
            normals[i] = p1.to(p2).left().normalize();
        }

        return normals;
    }

    public static float getWinding(Vec2... points) {
        int size = points.length;
        float area = 0f;
        for (int i = 0; i < size; i++) {
            Vec2 p1 = points[i];
            Vec2 p2 = points[i + 1 == size ? 0 : i + 1];
            area += p1.cross(p2);
        }
        // return the area
        return area;
    }

    public static void reverseWinding(Vec2... points) {
        int size = points.length;
        if (size == 1 || size == 0) return;
        int i = 0;
        int j = size - 1;
        Vec2 temp = null;
        while (j > i) {
            // swap
            temp = points[j];
            points[j] = points[i];
            points[i] = temp;
            // increment
            j--;
            i++;
        }
    }
}