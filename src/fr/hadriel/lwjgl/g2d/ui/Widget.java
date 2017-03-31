package fr.hadriel.lwjgl.g2d.ui;


import fr.hadriel.event.*;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.g2d.BatchRenderable;
import fr.hadriel.lwjgl.g2d.events.*;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Transform2D;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 07/02/2017.
 */
public abstract class Widget implements BatchRenderable {

    protected final EventListener listener;
//    protected final DeferredEventInterceptor interceptor;

    private boolean active;
    private boolean focused;
    private boolean focusable;
    private boolean hovered;
    private Group parent;

    protected final Vec2 size;
    private final Transform2D transform;
    private final Matrix3f absoluteMatrix;
    private final Matrix3f absoluteInverse;

    protected Widget() {
        this.listener = new EventListener();
//        this.interceptor = new DeferredEventInterceptor();
        this.focused = false;
        this.focusable = false;
        this.active = true;
        this.size = new Vec2();

        this.transform = new Transform2D();
        this.absoluteMatrix = new Matrix3f();
        this.absoluteInverse = new Matrix3f();
        computeAbsolute();

        //MouseMoved can generate MouseEnterEvent / MouseExitEvent on first valid / first invalid hitAbsolute detected
//        addEventFilter(MouseMovedEvent.class, (event) -> {
//            boolean hit = isHitAbsolute(event.x, event.y);
//            if (hit && !hovered) onEvent(new MouseEnterEvent(event.x, event.y));
//            if (!hit && hovered) onEvent(new MouseExitEvent(event.x, event.y));
//            return hit;
//        });
//        //MousePressed changes the focus of the current UIManager (may be changed if CPU intensive)
//        addEventFilter(MousePressedEvent.class, (event) -> {
//            boolean hit = isHitAbsolute(event.x, event.y);
//            if(hit && focusable && !focused) requestFocus();
//            return hit;
//        });
//        addEventFilter(FocusRequestEvent.class, (event) -> {
//            boolean gainFocus = event.widget == this;
//            //Virtual events interpretation.
//            if(gainFocus && !focused) listener.onEvent(new FocusGainEvent());
//            if(!gainFocus && focused) listener.onEvent(new FocusLostEvent());
//            return true;
//        });
//        addEventFilter(MouseReleasedEvent.class, (event) -> isHitAbsolute(event.x, event.y));

        //hovered is updated on Listen phase
        addEventHandler(MouseEnterEvent.class, (event) -> hovered = true);
        addEventHandler(MouseExitEvent.class, (event) -> hovered = false);
        //focused is updated on Listen phase
        addEventHandler(FocusLostEvent.class, (event) -> focused = false);
        addEventHandler(FocusGainEvent.class, (event) -> focused = true);
    }

    public boolean isHit(float x, float y) {
        return x >= 0 && x <= size.x && y >= 0 && y <= size.y;
    }

    public boolean isHitAbsolute(float x, float y) {
        Vec2 local = getAbsoluteInverse().multiply(new Vec2(x, y));
        return isHit(local.x, local.y);
    }

    public Vec2 getSize() {
        return size;
    }

    public void setSize(float width, float height) {
        size.set(width, height);
    }

    public Matrix3f getAbsoluteMatrix() {
        return absoluteMatrix;
    }

    public Matrix3f getAbsoluteInverse() {
        return absoluteInverse;
    }

    private void computeAbsolute() {
        absoluteMatrix.setToIdentity();
        if(parent != null) absoluteMatrix.multiply(parent.getAbsoluteMatrix());
        absoluteMatrix.multiply(transform.getMatrix());
        absoluteInverse.set(absoluteMatrix).invert();
    }

    public void setTransform(Matrix3f matrix) {
        transform.set(matrix);
        computeAbsolute();
    }

    public void translate(float x, float y) {
        transform.translate(x, y);
        computeAbsolute();
    }

    public void rotate(float angle) {
        transform.rotate(angle);
        computeAbsolute();
    }

    public void scale(float x, float y) {
        transform.scale(x, y);
        computeAbsolute();
    }

    public void onEvent(IEvent IEvent) {
        if(!active) return;
//        if(IEvent.isConsumed()) return;
//        if(!interceptor.accept(IEvent)) return;
//        if(IEvent.isListenable()) listener.onEvent(IEvent);
    }

    public void render(BatchGraphics g) {
        if(!active) return;
        g.push(transform.getMatrix());
        onRender(g);
        g.pop();
    }

    protected abstract void onRender(BatchGraphics g);

    /* Configuration / Accessors */

    public synchronized <T extends IEvent> void addEventHandler(Class<T> type, IEventHandler<T> handler) {
        listener.setEventHandler(type, handler);
    }

    public synchronized <T extends IEvent> void removeEventHandler(Class<T> type, IEventHandler<T> handler) {
//        listener.removeEventHandler(type, handler);
    }

//    public synchronized <T extends IEvent> void addEventFilter(Class<T> type, IEventFilter<T> filter) {
//        interceptor.addEventFilter(type, filter);
//    }
//
//    public synchronized <T extends IEvent> void removeEventFilter(Class<T> type, IEventFilter<T> filter) {
//        interceptor.removeEventFilter(type, filter);
//    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group nparent) {
        if(parent != null) parent.getWidgets().remove(this);
        this.parent = nparent;
        if(parent != null) parent.getWidgets().add(this);
        computeAbsolute();
    }

    //WARNING : no support of nested UIManagers
    public void requestFocus() {
        Widget current = this;
        while(current.parent != null) current = current.parent; // find the root.
        current.onEvent(new FocusRequestEvent(this));
        System.out.println(this + " is now Focus");
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean isFocusable() {
        return focusable;
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