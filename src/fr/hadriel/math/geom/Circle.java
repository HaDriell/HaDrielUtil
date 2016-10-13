package fr.hadriel.math.geom;

import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 06/09/2016.
 */
public class Circle extends Polygon {

    private static Vec2[] Generate(float radius, int vertexCount) {
        Vec2[] vertices = new Vec2[vertexCount];
        float unitAngle = 360f / (float) vertexCount;
        for(int i = 0;  i < vertexCount; i++) {
            vertices[i] = new Vec2(radius, 0).rotate(-90 - unitAngle * i);
        }
        return vertices;
    }

    public Circle(float radius, int vertexCount) {
        super(Generate(radius, vertexCount));
    }
}