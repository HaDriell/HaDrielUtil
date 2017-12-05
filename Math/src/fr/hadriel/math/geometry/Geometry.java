package fr.hadriel.math.geometry;

import fr.hadriel.math.Vec2;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public final class Geometry {

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