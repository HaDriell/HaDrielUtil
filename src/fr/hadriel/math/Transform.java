package fr.hadriel.math;

/**
 * Created by glathuiliere on 02/01/2017.
 */
public class Transform {

    private final Matrix4f matrix;
    private final Matrix4f inverse;
    private boolean valid;

    public Transform() {
        this.matrix = new Matrix4f();
        this.inverse = new Matrix4f();
    }

    public void invalidate() {
        valid = false;
    }

    public Vec2 transform(float x, float y) {
        if(!valid) {
            inverse.set(matrix).invert();
            valid = true;
        }
        return inverse.multiply(new Vec2(x, y));
    }

    public void set(Matrix4f matrix) {
        this.matrix.set(matrix);
        invalidate();
    }

    public void translate(float x, float y) {
        matrix.translate(x, y, 0);
        invalidate();
    }

    public void rotate(float r) {
        matrix.rotate(r, Vec3.Z);
        invalidate();
    }

    public void scale(float x, float y) {
        matrix.scale(x, y, 1);
        invalidate();
    }

    public Matrix4f getMatrix() {
        return new Matrix4f(matrix); // deep copy
    }
}