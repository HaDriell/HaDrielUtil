package fr.hadriel.graphics.gui;

import com.sun.istack.internal.NotNull;
import fr.hadriel.event.EventDispatcher;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;

public abstract class Widget implements IEventListener {

    private EventDispatcher dispatcher;
    private Matrix3 transform;
    private Vec2 size;

    protected Widget() {
        this.dispatcher = new EventDispatcher(true);
        this.transform = Matrix3.Identity;
        this.size = new Vec2();
    }

    public boolean hit(Vec2 v) {
        return hit(v.x, v.y);
    }

    public boolean hit(float x, float y) {
        Vec2 local = transform.multiplyInverse(x, y);
        return local.x >= 0 && local.y >= 0 && local.x <= size.x && local.y <= size.y;
    }

    public IEvent onEvent(IEvent event) {
        return dispatcher.onEvent(event);
    }

    public void onRender() {

    }

    public void setTransform(@NotNull Matrix3 transform) {
        this.transform = transform;
    }

    public Matrix3 getTransform() {
        return transform;
    }

    public void setSize(@NotNull Vec2 size) {
        this.size = size;
    }

    public Vec2 getSize() {
        return size;
    }
}