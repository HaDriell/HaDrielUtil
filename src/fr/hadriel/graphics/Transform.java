package fr.hadriel.graphics;

import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class Transform {

    private final Matrix3f matrix;
    private final Matrix3f matrixInverse;

    public Transform() {
        this.matrix = new Matrix3f();
        this.matrixInverse = new Matrix3f();
    }

    private void computeInverse() {
        matrixInverse.set(matrix.elements).invert();
    }

    public void setToTranslation(float x, float y) {
        matrix.setToTranslation(x, y);
        computeInverse();
    }

    public void setToRotation(float angle) {
        matrix.setToRotation(angle);
        computeInverse();
    }

    public void setToScale(float x, float y) {
        matrix.setToScale(x, y);
        computeInverse();
    }

    public void setTransform(float scaleX, float scaleY, float rotation, float positionX, float positionY) {
        matrix.setToTransform(scaleX, scaleY, rotation, positionX, positionY);
        computeInverse();
    }

    public void setMatrix(Matrix3f matrix) {
        this.matrix.set(matrix.elements);
        computeInverse();
    }

    public void translate(float x, float y) {
        matrix.translate(x, y);
        computeInverse();
    }

    public void scale(float x, float y) {
        matrix.scale(x, y);
        computeInverse();
    }

    public void rotate(float angle) {
        matrix.rotate(angle);
        computeInverse();
    }

    public Matrix3f toMatrix() {
        return new Matrix3f(matrix);
    }

    public void transform(Vec2 v) {
        v.transform(matrixInverse);
    }

    public void untransform(Vec2 v) {
        v.transform(matrix);
    }

}