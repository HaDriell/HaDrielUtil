package fr.hadriel.asset.graphics.gui;

import fr.hadriel.application.event.*;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.renderers.BatchGraphics;

public abstract class UIElement implements IEventListener {

    //State
    private UIContainer parent;
    public boolean enabled;

    //Bounds
    public float x;
    public float y;
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

    //package-private
    void setParent(UIContainer parent) {
        this.parent = parent;
    }

    public UIContainer getParent() {
        return parent;
    }

    public Vec2 getSize() { return new Vec2(width, height); }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        //TODO : signal layout should update
    }

    public void render(BatchGraphics graphics) {
        graphics.push(Matrix3.Translation(x, y));
        // TODO : if clipping can be enabled, add a clipping security
        onRender(graphics, width, height);
        graphics.pop();
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

    protected IEvent keyReleased(KeyReleasedEvent event) { return null; }
    protected IEvent keyPressed(KeyPressedEvent event) { return null; }
    protected IEvent mouseMoved(MouseMovedEvent event) { return null; }
    protected IEvent mousePressed(MousePressedEvent event) { return null; }
    protected IEvent mouseReleased(MouseReleasedEvent event) { return null; }
    protected IEvent mouseEntered(MouseEnteredEvent event) { return null; }
    protected IEvent mouseLeft(MouseLeftEvent event) { return null; }
    protected IEvent focusGained(FocusGainedEvent event) { return null; }
    protected IEvent focusLost(FocusLostEvent event) { return null; }

    protected abstract void onRender(BatchGraphics graphics, float width, float height);
}