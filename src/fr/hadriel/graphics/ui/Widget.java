package fr.hadriel.graphics.ui;

import fr.hadriel.events.*;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.HLRenderable;
import fr.hadriel.graphics.Transform;
import fr.hadriel.math.Vec2;

import java.awt.*;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class Widget implements HLRenderable {

    public boolean enabled;
    public boolean hovered;

    protected final Vec2 size;
    protected final Transform transform;

    protected Widget() {
        this.transform = new Transform();
        this.size = new Vec2();
        this.enabled = true;
    }

    public Transform getTransform() {
        return transform;
    }

    public float getWidth() {
        return size.x;
    }

    public float getHeight() {
        return size.y;
    }

    public void setSize(float x, float y) {
        size.set(x, y);
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

    /**
     * Called when mouse hits returns true for the first time on the Widget
     */
    public void onMouseEntered() {}

    /**
     * Called when mouse hits returns false for the first time on the Widget
     */
    public void onMouseExited() {}

    public void onRender(HLGraphics graphics) {}

    public void onUpdate(float delta) {}

    public boolean hit(Vec2 v) {
        Vec2 point = v.copy();
        transform.transform(point);
        boolean hit = isHit(point);
        if(hit && !hovered) onMouseEntered();
        if(!hit && hovered) onMouseExited();
        hovered = hit;
        return hovered;
    }

    protected boolean isHit(Vec2 point) {
        return !(point.x < 0 || point.x > size.x || point.y < 0 || point.y > size.y);
    }

    public final void render(HLGraphics graphics) {
        graphics.pushTransform(transform.toMatrix());
        graphics.pushClip(new Rectangle(0, 0, (int) size.x, (int) size.y));
        onRender(graphics);
        graphics.popClip();
        graphics.popTransform();
    }
}