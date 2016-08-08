package fr.hadriel.graphics.ui;

import fr.hadriel.events.*;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.HLRenderable;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class Widget implements HLRenderable {

    public boolean enabled;
    public final Vec2 size;

    protected Matrix3f transform;
    protected Matrix3f transformInverse;

    protected Widget() {
        this.transform = new Matrix3f();
        this.transformInverse = new Matrix3f();
        this.size = new Vec2();
        this.enabled = true;
    }

    public void setTransform(Matrix3f transform) {
        this.transform = transform.copy();
        this.transformInverse = transform.copy().invert();
    }

    public boolean onMouseMoved(MouseMovedEvent event) {
        return false;
    }

    public boolean onMousePressed(MousePressedEvent event) {
        return false;
    }

    public boolean onMouseReleased(MouseReleasedEvent event) {
        return false;
    }

    public boolean onKeyPressed(KeyPressedEvent event) {
        return false;
    }

    public boolean onKeyReleased(KeyReleasedEvent event) {
        return false;
    }

    public void onRender(HLGraphics graphics) {}


    public void onUpdate(float delta) {}

    public boolean hit(Vec2 v) {
        Vec2 point = v.copy();
        transformInverse.transform(point);
        return !(point.x < 0 || point.x > size.x || point.y < 0 || point.y > size.y);
    }

    public final void render(HLGraphics graphics) {
        graphics.push(transform);
        onRender(graphics);
        graphics.pop();
    }
}