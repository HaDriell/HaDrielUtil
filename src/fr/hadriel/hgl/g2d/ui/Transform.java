package fr.hadriel.hgl.g2d.ui;

import fr.hadriel.events.MouseMovedEvent;
import fr.hadriel.events.MousePressedEvent;
import fr.hadriel.events.MouseReleasedEvent;
import fr.hadriel.hgl.g2d.BatchGraphics;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;

/**
 * Created by glathuiliere on 13/12/2016.
 */
public class Transform extends Group {

    private final Vec2 point = new Vec2();
    private final Matrix4f matrix = new Matrix4f();
    private final Matrix4f inverse = new Matrix4f();
    private boolean valid = false;

    public Transform() {
        this(new Matrix4f());
    }

    public Transform(Matrix4f matrix) {
        set(matrix);

        setOn(MousePressedEvent.class, (event) -> {
            validate();
            point.set(event.x, event.y);
            inverse.multiply(point);
            onForward(new MousePressedEvent(point.x, point.y, event.button));
            return true;
        });

        setOn(MouseReleasedEvent.class, (event) -> {
            validate();
            point.set(event.x, event.y);
            inverse.multiply(point);
            onForward(new MouseReleasedEvent(point.x, point.y, event.button));
            return true;
        });

        setOn(MouseMovedEvent.class, (event) -> {
            validate();
            point.set(event.x, event.y);
            inverse.multiply(point);
            onForward(new MouseMovedEvent(point.x, point.y, event.dragged));
            return true;
        });
    }

    private void invalidate() {
        valid = false;
    }

    private void validate() {
        if(!valid) inverse.set(matrix).invert();
    }

    public void set(Matrix4f matrix) {
        this.matrix.set(matrix);
        invalidate();
    }

    public void set(float sx, float sy, float r, float tx, float ty) {
        matrix.setToTransform2D(sx, sy, r, tx, ty);
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

    public void onRender(BatchGraphics g) {
        g.push(matrix);
        super.onRender(g);
        g.pop();
    }
}