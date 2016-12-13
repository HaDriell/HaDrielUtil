package fr.hadriel.hgl.g2d;

import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;

/**
 * Created by HaDriel on 09/12/2016.
 */
public class Transform2D {
    private Matrix4f matrix;
    private boolean inversed;
    private Matrix4f inverse;

    public Transform2D() {
        this.matrix = new Matrix4f();
        this.inverse = new Matrix4f();
        inversed = true;
    }

    private void computeInverse() {
        this.inverse.set(matrix).invert();
    }

    public void set(Matrix4f matrix) {
        this.matrix.set(matrix); //deep copy
        inversed = false;
    }

    public void translate(float x, float y) {
        matrix.translate(x, y, 0);
        inversed = false;
    }

    public void rotate(float r) {
        matrix.rotate(r, Vec3.Z);
        inversed = false;
    }

    public void scale(float x, float y) {
        matrix.scale(x, y, 1);
        inversed = false;
    }

    public void setTransform(float sx, float sy, float r, float tx, float ty) {
        matrix.setToTransform2D(sx, sy, r, tx, ty);
        inversed = false;
    }

    public Matrix4f getMatrix() {
        return matrix;
    }

    public Vec2 getTransformed(Vec2 v) {
        return getTransformed(v.x, v.y);
    }

    public Vec2 getTransformed(float x, float y) {
        if(!inversed) inverse.set(matrix).invert(); // lazy inverting
        return inverse.multiply(new Vec2(x, y));
    }

    public Vec2 getUntransformed(Vec2 v) {
        return getUntransformed(v.x, v.y);
    }

    public Vec2 getUntransformed(float x, float y) {
        return matrix.multiply(new Vec2(x, y));
    }
}