package fr.hadriel.opengl;

import fr.hadriel.math.Matrix3;

import java.util.Stack;

/**
 * Created by HaDriel on 09/12/2016.
 */
public class MatrixStack {

    private Stack<Matrix3> stack;

    public MatrixStack() {
        this.stack = new Stack<>();
        this.stack.push(new Matrix3());
    }

    public void clear() {
        stack.setSize(1);
    }

    public Matrix3 top() {
        return stack.peek();
    }

    public void push(Matrix3 matrix) {
        stack.push(stack.peek().multiply(matrix));
    }

    public void pushOverride(Matrix3 matrix) {
        stack.push(matrix);
    }

    public void pop() {
        if(stack.size() <= 1) return;
        stack.pop();
    }
}
