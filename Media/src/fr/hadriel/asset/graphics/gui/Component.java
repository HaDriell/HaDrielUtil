package fr.hadriel.asset.graphics.gui;

import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.renderers.BatchGraphics;

public abstract class Component implements IEventListener {

    private boolean enabled;
    private Container parent;
    private Matrix3 transform;
    private Vec2 size;

    protected Component() {
        this.enabled = true;
        this.parent = null;
        this.transform = Matrix3.Identity;
        this.size = new Vec2();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setParent(Container parent) {
        if (this.parent != null) this.parent.getChildren().remove(this);
        this.parent = parent;
        if (this.parent != null) this.parent.getChildren().add(this);
    }

    public Container getParent() {
        return parent;
    }

    public Component hit(Vec2 v) {
        return hit(v.x, v.y);
    }

    public Component hit(float x, float y) {
        if (enabled) {
            Vec2 local = transform.multiplyInverse(x, y);
            if (local.x >= 0 && local.y >= 0 && local.x <= size.x && local.y <= size.y)
                return this;
        }
        return null;
    }

    public void setTransform(Matrix3 transform) {
        this.transform = transform;
    }

    public Matrix3 getTransform() {
        return transform;
    }

    public void setSize(Vec2 size) {
        this.size = size;
    }

    public Vec2 getSize() {
        return size;
    }

    public void render(BatchGraphics graphics) {
        if (enabled) {
            graphics.push(transform);
            onRender(graphics);
            graphics.pop();
        }
    }


    public abstract IEvent onEvent(IEvent event);

    protected abstract void onRender(BatchGraphics graphics);
}