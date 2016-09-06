package fr.hadriel.math.geom;

import fr.hadriel.math.Interval;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 05/09/2016.
 */
public class Polygon {

    private Vec2[] vertices;
    private Vec2[] normals;

    public Polygon(Vec2... vertices) {
        setVertices(vertices);
    }

    public Polygon(Polygon p) {
        setVertices(p.vertices);
    }

    public void setVertices(Vec2[] vertices) {
        int length = vertices.length;
        if(length < 3) throw new IllegalArgumentException("Not enough vertices (" + length + " < 3)");

        this.vertices = new Vec2[length];
        this.normals = new Vec2[length];
        for(int i = 0; i < length; i++) {
            Vec2 v0 = vertices[i].copy();
            this.vertices[i] = vertices[i].copy();
            this.normals[i] = vertices[(i + 1) % length].copy().sub(v0).normalize();
        }
    }

    public Polygon copy() {
        return new Polygon(this);
    }

    public Polygon rotate(float angle) {
        for(Vec2 v : vertices)
            v.rotate(angle);
        return this;
    }

    public Polygon translate(float x, float y) {
        for(Vec2 v : vertices)
            v.add(x, y);
        return this;
    }

    public Polygon scale(float x, float y) {
        for(Vec2 v : vertices)
            v.scale(x, y);
        return this;
    }

    public Polygon transform(Matrix3f matrix) {
        Vec2[] transformedVertices = new Vec2[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            transformedVertices[i] = vertices[i].getTransformed(matrix);
        }
        setVertices(transformedVertices);
        return this;
    }

    public Interval getProjection(Vec2 axis) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for(Vec2 v : vertices) {
            float p = v.dot(axis);
            if(p < min) min = p;
            if(p > max) max = p;
        }
        return new Interval(min, max);
    }

    public Vec2[] getVertices() {
        return vertices;
    }

    public Vec2[] getNormals() {
        return normals;
    }
}