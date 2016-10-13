package fr.hadriel.math.geom;

import fr.hadriel.math.Collisions;
import fr.hadriel.math.Interval;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;

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
            this.normals[i] = vertices[(i + 1) % length].copy().sub(v0).getNormalLeft();
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

    //a is the Clipping Polygon, b is the Clipped Polygon
    public static Polygon intersection(Polygon a, Polygon b) {
        Vec2[] aVertices = a.getVertices();
        Vec2[] vertices = b.getVertices();
        List<Vec2> outputList = new ArrayList<>(vertices.length);
        for(Vec2 v : vertices) outputList.add(v);

        //
        for(int i=0; i < aVertices.length; i++) {
            if(outputList.size() == 0) break; // quick end
            Vec2 eA = aVertices[i];
            Vec2 eB = aVertices[(i + 1) % aVertices.length];
            List<Vec2> inputList = new ArrayList<>(outputList);
            outputList.clear();
            Vec2 s = inputList.get(inputList.size() - 1); // last
            for(Vec2 e : inputList) {
                boolean eInside = Collisions.pointOnLine(e, eA, eB) != Collisions.ABOVE;
                boolean sInside = Collisions.pointOnLine(s, eA, eB) != Collisions.ABOVE;
                Vec2 intersection = Collisions.lineToLine(e, s, eA, eB);

                if(eInside) {
                    if(!sInside) {
                        outputList.add(intersection);
                    }
                    outputList.add(e);
                } else if (sInside) {
                    outputList.add(intersection);
                }
                s = e;
            }
        }
        if(outputList.size() < 3)
            return null;
        vertices = new Vec2[outputList.size()];
        outputList.toArray(vertices);
        return new Polygon(vertices);
    }

    public Vec2 getCentroid() {
        Vec2 centroid = new Vec2();
        Vec2 v0, v1;
        float a;
        float signedArea = 0;
        for(int i = 0; i < vertices.length - 1; i++) {
            v0 = vertices[i];
            v1 = vertices[i + 1];
            a = v0.cross(v1);
            signedArea += a;
            centroid.x += (v0.x + v1.x) * a;
            centroid.y += (v0.y + v1.y) * a;
        }

        // last point computation
        v0 = vertices[vertices.length - 1];
        v1 = vertices[0];
        a = v0.cross(v1);

        signedArea += a;
        centroid.x += (v0.x + v1.x) * a;
        centroid.y += (v0.y + v1.y) * a;

        signedArea = 1 / (3 * signedArea);
        centroid.scale(signedArea, signedArea);
        return centroid;
    }
}