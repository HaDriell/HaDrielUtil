package fr.hadriel.lwjgl.g2d.ui;

import fr.hadriel.event.*;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.g2d.BatchRenderable;
import fr.hadriel.math.Vec2;
import fr.hadriel.util.Property;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public abstract class Node implements BatchRenderable, IEventListener {

    private boolean visible;

    private Property<Vec2> sizeProperty;
    private MultiEventListener listener;
    private IEventHandler<IEvent> defaultHandler;

    protected Node() {
        this.sizeProperty = new Property<>(new Vec2(), new Vec2());
        this.visible = true;
        this.listener = new MultiEventListener();
    }

    public boolean isInside(float x, float y) {
        Vec2 size = sizeProperty.get();
        boolean hit = (x >= 0 && x <= size.x && y >= 0 && y < size.y);
        return hit;
    }

    public <T extends IEvent> void setOn(Class<T> type, IEventHandler<T> handler) {
        listener.setOn(type, handler);
    }

    public void setOnDefault(IEventHandler<IEvent> handler) {
        defaultHandler = handler;
    }

    public float getWidth() {
        return sizeProperty.get().x;
    }

    public float getHeight() {
        return sizeProperty.get().y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setSize(float x, float y) {
        sizeProperty.set(new Vec2(x, y));
    }

    public final boolean onEvent(IEvent event) {
        return listener.onEvent(event);
    }

    public final boolean onDefault(IEvent event) {
        return defaultHandler != null && defaultHandler.handle(event);
    }

    public void render(BatchGraphics g) {
        if(visible) onRender(g);
    }

    public abstract void onRender(BatchGraphics g);
}