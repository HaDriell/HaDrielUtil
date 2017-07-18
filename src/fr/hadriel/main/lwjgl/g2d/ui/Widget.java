package fr.hadriel.main.lwjgl.g2d.ui;


import fr.hadriel.main.event.*;
import fr.hadriel.main.lwjgl.g2d.BatchGraphics;
import fr.hadriel.main.lwjgl.g2d.BatchRenderable;
import fr.hadriel.main.lwjgl.g2d.event.*;
import fr.hadriel.main.math.Matrix3f;
import fr.hadriel.main.math.Transform2D;
import fr.hadriel.main.math.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 07/02/2017.
 */
public abstract class Widget implements BatchRenderable {

    //Event Framework Implementation
    private List<IEventListener> bubbleListeners;
    private List<IEventListener> captureListeners;

    //GUI states
    private Group parent;
    private boolean active;
    private boolean focused;
    private boolean hovered;
    private final Vec2 size;
    private final Transform2D transform;
    private boolean valid;
    private final Matrix3f absoluteMatrix;
    private final Matrix3f absoluteInverse;

    protected Widget() {
        this.bubbleListeners = new ArrayList<>();
        this.captureListeners = new ArrayList<>();

        this.parent = null;
        this.active = true;
        this.focused = false;
        this.hovered = false;
        this.size = new Vec2();
        this.transform = new Transform2D();
        this.valid = false;
        this.absoluteMatrix = new Matrix3f();
        this.absoluteInverse = new Matrix3f();
        //Initialize the internal behaviors
        MultiEventListener listener = new MultiEventListener();
        listener.setEventHandler(MouseEnterEvent.class, this::handle);
        listener.setEventHandler(MouseExitEvent.class, this::handle);
        listener.setEventHandler(MouseMovedEvent.class, this::handle);
        listener.setEventHandler(FocusChangedEvent.class, this::handle);
        addEventListener(listener, true);
    }

    private UIEvent handle(MouseMovedEvent event) {
        boolean inside = isInsideAbsolute(event.x, event.y);
        if(inside) event.capture();
        if(inside && !hovered) onEvent(new MouseEnterEvent(event.x, event.y));
        if(!inside && hovered) onEvent(new MouseExitEvent(event.x, event.y));
        return event;
    }

    private UIEvent handle(MouseEnterEvent event) {
        hovered = true;
        return event;
    }

    private UIEvent handle(MouseExitEvent event) {
        hovered = false;
        return event;
    }

    private UIEvent handle(FocusChangedEvent event) {
        focused = event.widget == this;
        return event;
    }

    public boolean isInsideRelative(float x, float y) {
        return x >= 0 && x <= size.x && y >= 0 && y <= size.y;
    }

    public boolean isInsideAbsolute(float x, float y) {
        Vec2 local = getAbsoluteInverse().multiply(new Vec2(x, y));
        return isInsideRelative(local.x, local.y);
    }

    public Matrix3f getAbsoluteMatrix() {
        validate();
        return absoluteMatrix;
    }

    public Matrix3f getAbsoluteInverse() {
        validate();
        return absoluteInverse;
    }

    private void validate() {
        if(!valid) {
            absoluteMatrix.setToIdentity();
            if (parent != null) absoluteMatrix.multiply(parent.getAbsoluteMatrix());
            absoluteMatrix.multiply(transform.getMatrix());
            absoluteInverse.set(absoluteMatrix).invert();
            valid = true;
        }
    }

    public void invalidate() {
        valid = false;
    }

    public void setTransform(Matrix3f matrix) {
        transform.set(matrix);
        invalidate();
    }

    public void translate(float x, float y) {
        transform.translate(x, y);
        invalidate();
    }

    public void rotate(float angle) {
        transform.rotate(angle);
        invalidate();
    }

    public void scale(float x, float y) {
        transform.scale(x, y);
        invalidate();
    }

    public void render(BatchGraphics g) {
        if(active) {
            g.push(transform.getMatrix());
            onRender(g, size.x, size.y);
            g.pop();
        }
    }

    protected abstract void onRender(BatchGraphics g, float width, float height);

    public void onEvent(UIEvent event) {
        //If Consumed, stop propagation
        if(!active || event == null || event.isConsummed()) return; //skip dispatch

        //Fire the Event to the current Node using the correct set of Listeners
        Phase currentPhase = event.phase();
        List<IEventListener> listeners = (event.isCapturing() ? captureListeners : bubbleListeners);
        for(IEventListener listener : listeners) {
            if(event.phase() != currentPhase) break;
            listener.onEvent(event);
        }

        //When Bubbling, the Event is fired to the Parent if not handled
        if(event.isBubbling() && parent != null) parent.onEvent(event);
    }

        /* Configuration / Accessors */

    public void addEventListener(IEventListener listener, boolean onCapturePhase) {
        if(listener != null)
            (onCapturePhase ? captureListeners : bubbleListeners).add(listener);
    }

    public void removeEventListener(IEventListener listener, boolean onCapturePhase) {
        if(listener != null)
            (onCapturePhase ? captureListeners : bubbleListeners).remove(listener);
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group nparent) {
        if(parent != null) parent.getWidgets().remove(this);
        this.parent = nparent;
        if(parent != null) parent.getWidgets().add(this);
        invalidate();
    }


    public Vec2 getSize() {
        return size;
    }

    public void setSize(float width, float height) {
        size.set(width, height);
    }

    public boolean isFocused() {
        return focused;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHovered() {
        return hovered;
    }
}