package fr.hadriel.lwjgl.g2d;

import fr.hadriel.math.Matrix4f;

import java.util.Stack;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public class Matrix4fStack {

    private Stack<Matrix4f> stack;

    public Matrix4fStack() {
        this.stack = new Stack<>();
        this.stack.push(new Matrix4f());
    }

    public void clear() {
        stack.setSize(1);
    }

    public Matrix4f top() {
        return stack.peek();
    }

    public void push(Matrix4f matrix) {
        stack.push(stack.peek().copy().multiply(matrix));
    }

    public void pushOverride(Matrix4f matrix) {
        stack.push(matrix);
    }

    public void pop() {
        if(stack.size() <= 1) return;
        stack.pop();
    }
}
