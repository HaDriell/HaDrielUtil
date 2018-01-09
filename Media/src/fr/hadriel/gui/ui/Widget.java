package fr.hadriel.gui.ui;


import fr.hadriel.event.IEventListener;
import fr.hadriel.event.MultiEventListener;
import fr.hadriel.graphics.IGraphics;
import fr.hadriel.gui.event.*;
import fr.hadriel.graphics.IDrawable;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 07/02/2017.
 */
public abstract class Widget implements IDrawable {

    //Event Framework Implementation
    private List<IEventListener> bubbleListeners;
    private List<IEventListener> captureListeners;

    //GUI states
    private Group parent;
    private boolean enbaled;
    private Vec2 size;
    private final Matrix3f transform;
    private boolean valid;
    private final Matrix3f absoluteMatrix;
    private final Matrix3f absoluteInverse;

    protected Widget() {
        this(0, 0);
    }

    protected Widget(float width, float height) {
        this.bubbleListeners = new ArrayList<>();
        this.captureListeners = new ArrayList<>();

        this.parent = null;
        this.enbaled = true;
        this.size = new Vec2(width, height);
        this.transform = new Matrix3f();
        this.valid = false;
        this.absoluteMatrix = new Matrix3f();
        this.absoluteInverse = new Matrix3f();
        //Initialize the internal behaviors
        MultiEventListener internalInterceptor = new MultiEventListener();
        internalInterceptor.on(FocusGainEvent.class, this::handle);
        internalInterceptor.on(FocusLostEvent.class, this::handle);
        internalInterceptor.on(MouseEnterEvent.class, this::handle);
        internalInterceptor.on(MouseExitEvent.class, this::handle);
        internalInterceptor.on(MouseMovedEvent.class, this::handle);
        internalInterceptor.on(MousePressedEvent.class, this::handle);
        addEventListener(internalInterceptor, true);
    }

    private UIEvent handle(MousePressedEvent event) {
        UIContext context = requireUIContext();
        if (isInsideAbsolute(event.x, event.y)) {
            if(!context.isFocused(this))
                context.setFocused(this);
            event.capture();
        }
        return event;
    }

    private UIEvent handle(MouseMovedEvent event) {
        UIContext context = requireUIContext();
        if(isInsideAbsolute(event.x, event.y)) {
            if(!context.isHovered(this))
                context.setHovered(this);
            event.capture();
        }
        return event;
    }

    private UIEvent handle(MouseEnterEvent event) {
        event.capture();
        return event;
    }

    private UIEvent handle(MouseExitEvent event) {
        event.capture();
        return event;
    }

    private UIEvent handle(FocusGainEvent event) {
        event.capture();
        return event;
    }

    private UIEvent handle(FocusLostEvent event) {
        event.capture();
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
            absoluteMatrix.multiply(transform);
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

    public void transform(Matrix3f matrix3f) {
        transform.multiply(matrix3f);
    }

    public void draw(IGraphics g) {
        if(enbaled) {
            g.push(transform);
            onRender(g, size.x, size.y, requireUIContext());
            g.pop();
        }
    }

    protected abstract void onRender(IGraphics g, float width, float height, UIContext context);

    protected void onEventCapture(UIEvent event) {
        for(IEventListener listener : captureListeners) {
            if(!event.isCapturing())
                break;
            listener.onEvent(event);
        }
    }

    protected void onEventBubble(UIEvent event) {
        for(IEventListener listener : bubbleListeners) {
            if(!event.isBubbling())
                break;
            listener.onEvent(event);
        }
    }

    public final void onEvent(UIEvent event) {
        //If Consumed, stop propagation
        if(!enbaled || event == null || event.isConsummed()) return; //skip dispatch

        //Try to capture the event using Capture Listeners
        if(event.isCapturing()) {
            onEventCapture(event);
        }

        if(event.isBubbling()) {
            onEventBubble(event);
        }
    }

        /* Configuration / Accessors */


    public void addEventListener(IEventListener listener) {
        addEventListener(listener, false);
    }

    public void addEventListener(IEventListener listener, boolean onCapturePhase) {
        if(listener == null) return;
        if(onCapturePhase)
            captureListeners.add(listener);
        else
            bubbleListeners.add(listener);
    }

    public void rmeoveEventListener(IEventListener listener) {
        removeEventListener(listener, false);
    }

    public void removeEventListener(IEventListener listener, boolean onCapturePhase) {
        if(listener == null) return;
        if(onCapturePhase)
            captureListeners.remove(listener);
        else
            bubbleListeners.remove(listener);
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

    public UIContext getUIContext() {
        return parent == null ? null : parent.getUIContext();
    }

    private UIContext requireUIContext() {
        UIContext context = getUIContext();
        if(context == null)
            throw new RuntimeException("No UIContext setup for the Widget when required" + this);
        return context;
    }

    public Vec2 getSize() {
        return size;
    }

    public void setSize(float width, float height) {
        size = new Vec2(width, height);
    }

    public boolean isEnbaled() {
        return enbaled;
    }

    public void setEnbaled(boolean enbaled) {
        this.enbaled = enbaled;
    }
}