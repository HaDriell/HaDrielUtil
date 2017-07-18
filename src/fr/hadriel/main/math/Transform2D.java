package fr.hadriel.main.math;

/**
 * Created by glathuiliere on 02/01/2017.
 */
public class Transform2D {

    private final Matrix3f matrix;
    private final Matrix3f inverse;
    private boolean valid;

    public Transform2D(Matrix3f matrix) {
        this.matrix = matrix == null ? new Matrix3f() : new Matrix3f(matrix);
        this.inverse = new Matrix3f();
        this.valid = false;
    }

    public Transform2D() {
        this(new Matrix3f());
    }

    private void invalidate() {
        valid = false;
    }

    private void validate() {
        if(!valid) {
            inverse.set(matrix).invert();
            valid = true;
        }
    }

    public Vec2 transform(Vec2 v) {
        validate();
        return inverse.multiply(v);
    }

    public Vec2 transform(float x, float y) {
        return transform(new Vec2(x, y));
    }

    public Vec2 untransform(Vec2 v) {
        return matrix.multiply(v);
    }

    public Vec2 untransform(float x, float y) {
        return untransform(new Vec2(x, y));
    }

    public void set(Matrix3f matrix) {
        this.matrix.set(matrix);
        invalidate();
    }

    public void translate(float x, float y) {
        matrix.translate(x, y);
        invalidate();
    }

    public void rotate(float r) {
        matrix.rotate(r);
        invalidate();
    }

    public void scale(float x, float y) {
        matrix.scale(x, y);
        invalidate();
    }

    public Matrix3f getMatrix() {
        return new Matrix3f(matrix); // deep copy
    }

    public Matrix3f getInverse() {
        validate();
        return new Matrix3f(inverse);
    }
}