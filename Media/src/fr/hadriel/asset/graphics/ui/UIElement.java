package fr.hadriel.asset.graphics.ui;

import fr.hadriel.application.event.*;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.renderers.BatchGraphics;

public abstract class UIElement implements IEventListener {

    //State
    //package-private
    UIContainer parent;
    private boolean enabled;

    //Bounds
    private float x;
    private float y;
    private float width;
    private float height;

    protected UIElement() {
        this.enabled = true;
    }

    public final boolean hit(float x, float y) {
        float dx = x - this.x;
        float dy = y - this.y;
        return enabled && dx >= 0 && dx <= width && dy >= 0 && dy <= height;
    }

    public UIContainer getParent() {
        return parent;
    }

    public void requestFocus() {
        requestFocus(this);
    }

    public void requestFocus(UIElement element) {
        if (parent != null) parent.requestFocus(element);
    }

    public void invalidateLayout() {
        if (parent != null) parent.invalidateLayout();
    }

    public UIElement capture(float x, float y) {
        return hit(x, y) ? this : null;
    }

    public Vec2 getPosition() {
        return new Vec2(x, y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        invalidateLayout();
    }

    public Vec2 getSize() { return new Vec2(width, height); }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        invalidateLayout();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        invalidateLayout();
    }

    public void render(BatchGraphics graphics) {
        if (enabled) {
            graphics.push(Matrix3.Translation(x, y));
            // TODO : if clipping can be enabled, add a clipping security
            onRender(graphics);
            graphics.pop();
        }
    }

    public IEvent onEvent(IEvent event) {
        //Key handling
        event = IEvent.dispatch(event, KeyPressedEvent.class,    this::keyPressed);
        event = IEvent.dispatch(event, KeyReleasedEvent.class,   this::keyReleased);
        //Mouse handling
        event = IEvent.dispatch(event, MouseEnteredEvent.class,  this::mouseEntered);
        event = IEvent.dispatch(event, MouseLeftEvent.class,     this::mouseLeft);
        event = IEvent.dispatch(event, MouseMovedEvent.class,    this::mouseMoved);
        event = IEvent.dispatch(event, MousePressedEvent.class,  this::mousePressed);
        event = IEvent.dispatch(event, MouseReleasedEvent.class, this::mouseReleased);
        //Focus handling
        event = IEvent.dispatch(event, FocusGainedEvent.class,   this::focusGained);
        event = IEvent.dispatch(event, FocusLostEvent.class,     this::focusLost);
        return event;
    }

    protected IEvent keyReleased(KeyReleasedEvent event) { return event; }
    protected IEvent keyPressed(KeyPressedEvent event) { return event; }
    protected IEvent mouseMoved(MouseMovedEvent event) { return event; }
    protected IEvent mousePressed(MousePressedEvent event) { return event; }
    protected IEvent mouseReleased(MouseReleasedEvent event) { return event; }
    protected IEvent mouseEntered(MouseEnteredEvent event) { return event; }
    protected IEvent mouseLeft(MouseLeftEvent event) { return event; }
    protected IEvent focusGained(FocusGainedEvent event) { return event; }
    protected IEvent focusLost(FocusLostEvent event) { return event; }

    protected abstract void onRender(BatchGraphics graphics);
}