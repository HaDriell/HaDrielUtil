package fr.hadriel.lwjgl.g2d;

import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Matrix4f;

import java.util.Stack;

/**
 * Created by HaDriel on 09/12/2016.
 */
public class Matrix3fStack {

    private Stack<Matrix3f> stack;

    public Matrix3fStack() {
        this.stack = new Stack<>();
        this.stack.push(new Matrix3f());
    }

    public void clear() {
        stack.setSize(1);
    }

    public Matrix3f top() {
        return stack.peek();
    }

    public void push(Matrix3f matrix) {
        stack.push(stack.peek().copy().multiply(matrix));
    }

    public void pushOverride(Matrix3f matrix) {
        stack.push(matrix);
    }

    public void pop() {
        if(stack.size() <= 1) return;
        stack.pop();
    }
}
