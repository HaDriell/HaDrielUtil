package fr.hadriel.graphics;

import java.awt.*;
import java.util.Stack;

/**
 * Created by glathuiliere setOn 11/08/2016.
 */
public class ClipStack {

    private Stack<Shape> stack;

    public ClipStack(Shape defaultShape) {
        this.stack = new Stack<>();
        this.stack.push(defaultShape);
    }

    public void pop() {
        if(stack.size() > 1) {
            stack.pop();
        }
    }

    public void push(Shape shape) {
        stack.push(shape);
    }

    public Shape top() {
        return stack.peek();
    }
}
