package fr.hadriel.graphics;

import fr.hadriel.math.Matrix3f;

import java.awt.geom.AffineTransform;
import java.util.Stack;

/**
 * Created by glathuiliere on 22/07/2016.
 */
public class MatrixStack {

    private Stack<Matrix3f> matrices;

    public MatrixStack() {
        this.matrices = new Stack<>();
        clear();
    }

    public AffineTransform topTransform() {
        return matrices.peek().toAffineTransform();
    }

    public Matrix3f top() {
        return matrices.peek();
    }

    public void push(Matrix3f matrix) {
        matrices.push(top().copy().multiply(matrix));
    }

    public void pushOverride(Matrix3f matrix) {
        matrices.push(matrix.copy());
    }

    public void pop() {
        if(matrices.size() > 1)
            matrices.pop();
    }

    public void clear() {
        matrices.clear();
        matrices.push(new Matrix3f());
    }
}
