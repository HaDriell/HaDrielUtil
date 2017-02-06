package fr.hadriel.lwjgl.g2d.scene;

import fr.hadriel.event.*;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.g2d.BatchRenderable;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Transform2D;

/**
 * Created by glathuiliere on 06/02/2017.
 */
public abstract class Widget implements BatchRenderable {

    private DeferredEventFilter filter;
    private DeferredEventListener listener;

    private boolean active;

    private Group parent;

    //2D transform implementation
    private Transform2D transform;
    private Matrix3f absoluteInverse;
    private boolean absoluteValid;

    public Widget() {
        this.active = true;
        this.filter = new DeferredEventFilter();
        this.listener = new DeferredEventListener();
        this.transform = new Transform2D();
        this.absoluteInverse = new Matrix3f();
        this.absoluteValid = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void invalidateAbsoluteInverse() {
        this.absoluteValid = false;
    }

    public Widget getParent() {
        return parent;
    }

    public Matrix3f getAbsoluteInverse() {
        if(!absoluteValid) {
            if(parent != null) {
                absoluteInverse.set(parent.getAbsoluteInverse());
            } else {
                absoluteInverse.setIdentity();
            }
            absoluteInverse.multiply(transform.getInverse());
        }
        return new Matrix3f(absoluteInverse);
    }

    //TODO : change setParent / add(Widget w) / remove(Widget w) to fix the parentability between components
    public void setParent(Widget parent) {
        if(this.parent != null) {
        }
        this.parent = parent;
        invalidateAbsoluteInverse();
    }

    public void translate(float x, float y) {
        transform.translate(x, y);
        invalidateAbsoluteInverse();
    }

    public void rotate(float angle) {
        transform.rotate(angle);
        invalidateAbsoluteInverse();
    }

    public void scale(float x, float y) {
        transform.scale(x, y);
        invalidateAbsoluteInverse();
    }

    public <T extends Event> void addEventFilter(Class<T> type, IEventFilter<T> eventFilter) {
        filter.addEventFilter(type, eventFilter);
    }

    public <T extends Event> void removeEventFilter(Class<T> type, IEventFilter<T> eventFilter) {
        filter.removeEventFilter(type, eventFilter);
    }

    public <T extends Event> void addEventHandler(Class<T> type, IEventHandler<T> eventHandler) {
        listener.addEventHandler(type, eventHandler);
    }

    public <T extends Event> void removeEventHandler(Class<T> type, IEventHandler<T> eventHandler) {
        listener.removeEventHandler(type, eventHandler);
    }

    public void onEvent(Event event) {
        if(!active || event.isConsumed())
            return;
        if(filter.accept(event)) {
            listener.onEvent(event);
        }
    }

    public abstract void onRender(BatchGraphics g);

    public void render(BatchGraphics g) {
        g.push(transform.getMatrix());
        onRender(g);
        g.pop();
    }
}