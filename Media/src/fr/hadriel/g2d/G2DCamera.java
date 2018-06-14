package fr.hadriel.g2d;

import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix4;
import fr.hadriel.math.Vec2;

public class G2DCamera {

    private Vec2 scale;
    private Vec2 position;
    private float angle;

    private float left;
    private float top;
    private float right;
    private float bottom;

    public G2DCamera(float left, float top, float right, float bottom) {
        this.scale = Vec2.XY;
        this.position = Vec2.ZERO;
        this.angle = 0;

        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void setPosition(Vec2 v) { setPosition(v.x, v.y); }
    public void setPosition(float x, float y) {
        this.position = new Vec2(x, y);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }


    public void setScale(Vec2 v) { setScale(v.x, v.y); }
    public void setScale(float x, float y) {
        this.scale = new Vec2(x, y);
    }

    public void translate(Vec2 v) { translate(v.x, v.y); }
    public void translate(float x, float y) {
        position = position.add(x, y);
    }

    public void rotate(float angle) {
        this.angle += angle;
    }

    public void scale(Vec2 v) { translate(v.x, v.y); }
    public void scale(float x, float y) {
        scale = scale.add(x, y);
    }

    public Matrix3 getViewTransform() {
        return Matrix3.Transform(scale.x, scale.y, angle, position.x, position.y);
    }

    public Matrix4 getProjectionTransform() {
        return Matrix4.Orthographic(left, right, top, bottom, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

}