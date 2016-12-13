package fr.hadriel.hgl.g2d;

import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.event.MultiEventListener;
import fr.hadriel.math.Vec2;
import fr.hadriel.util.Property;

/**
 * Created by HaDriel on 09/12/2016.
 */
public abstract class Widget implements BatchRenderable, IEventListener {

    public boolean visible;
    private boolean hovered;

    private Property<Vec2> sizeProperty;
    private final Transform2D transform;
    private MultiEventListener listener;

    protected Widget() {
        this.transform = new Transform2D();
        this.sizeProperty = new Property<>(new Vec2(), new Vec2());
        this.visible = true;
        this.listener = new MultiEventListener();
        setupCallbacks(listener);
    }

    protected boolean isHit(Vec2 v) {
        return v.x > 0 && v.x <= sizeProperty.get().x &&
                v.y > 0 && v.y <= sizeProperty.get().y;
    }

    public boolean hit(Vec2 v) {
        Vec2 tv = transform.getTransformed(v);
        boolean hit = isHit(tv);
        if(hit && !hovered) {
            //fire mouseEntered
        }
        if(!hit && hovered) {
            //fire mouseExited
        }
        hovered = hit;
        return hit;
    }

    public Transform2D getTransform() {
        return transform;
    }

    public float getWidth() {
        return sizeProperty.get().x;
    }

    public float getHeight() {
        return sizeProperty.get().y;
    }

    public void setSize(float x, float y) {
        sizeProperty.set(new Vec2(x, y));
    }

    public boolean onEvent(IEvent event) {
        return listener.onEvent(event);
    }

    public final void render(BatchGraphics g) {
        g.push(transform.getMatrix());
        onRender(g);
        g.pop();
    }

    protected abstract void onRender(BatchGraphics g);
    protected abstract void setupCallbacks(MultiEventListener listener);
}