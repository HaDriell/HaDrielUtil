package fr.hadriel.asset.graphics.ui;

import fr.hadriel.application.event.*;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.renderer.BatchGraphics;

public abstract class UIElement implements IEventListener {

    //State
    //package-private
    UIContainer parent;
    private boolean enabled;
    private boolean valid;

    //Bounds
    private float x;
    private float y;
    private float width;
    private float height;

    protected UIElement() {
        this.enabled = true;
        this.valid = false;
    }

    public final boolean hit(float x, float y) {
        validate();
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

    public UIElement capture(float x, float y) {
        return hit(x, y) ? this : null;
    }

    public Vec2 getPosition() {
        return new Vec2(x, y);
    }

    public void setPosition(float x, float y) {
        if (this.x != x || this.y != y) {
            System.out.println("Updating Position " + getPosition() + " to " + new Vec2(x, y));
            invalidate();
        }
        this.x = x;
        this.y = y;
    }

    public Vec2 getSize() { return new Vec2(width, height); }

    public void setSize(float width, float height) {
        if (this.width != width || this.height != height) {
            System.out.println("Updating Size " + getSize() + " to " + new Vec2(width, height));
            invalidate();
        }
        this.width = width;
        this.height = height;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) invalidate();
        this.enabled = enabled;
    }

    /* UIElement validation (enable & size & position) */

    public final void invalidate() {
        if(!valid) return;
        valid = false;
        if (parent != null) parent.invalidate();
    }

    public final void validate() {
        if (valid) return;
        onValidate();
        valid = true;
    }

    //Customizable behavior on Validation
    protected void onValidate() { }

    /* render mechanic section */

    public final void render(BatchGraphics graphics) {
        if (enabled) {
            validate();
            graphics.push(Matrix3.Translation(x, y));
            // TODO : setup clipping when supported
            onRender(graphics);
            graphics.pop();
        }
    }

    //Customizable behavior on Sprite
    protected abstract void onRender(BatchGraphics graphics);


    /* event handling section */

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

}