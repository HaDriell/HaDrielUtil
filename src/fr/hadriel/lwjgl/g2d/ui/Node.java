package fr.hadriel.lwjgl.g2d.ui;

import fr.hadriel.event.ComplexEventListener;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventHandler;
import fr.hadriel.event.IEventListener;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.g2d.BatchRenderable;
import fr.hadriel.lwjgl.g2d.events.*;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Transform2D;
import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 01/02/2017.
 */
public abstract class Node implements BatchRenderable, IEventListener {

    //Event handling
    private ComplexEventListener captureListener;
    private ComplexEventListener bubbleListener;

    //Node tree infos
    private Node parent;
    private List<Node> children;

    //Node 2D infos
    private Transform2D transform;
    private Matrix3f absoluteInverseMatrix;
    private boolean absoluteInverseMatrixValid;

    private Vec2 size;

    //Node states
    private boolean focus;
    private boolean hovered;
    private boolean visible;

    public Node(boolean useDefaultHandlers) {
        this.captureListener = new ComplexEventListener();
        this.bubbleListener = new ComplexEventListener();
        this.children = new ArrayList<>();
        this.transform = new Transform2D();
        this.absoluteInverseMatrix = new Matrix3f();
        this.absoluteInverseMatrixValid = false;
        this.size = new Vec2();
        this.visible = true;

        //Default Handlers
        if(useDefaultHandlers) {

            /* Handles that manages the mouse input listening and virtual related events */

            addHandler(MouseMovedEvent.class, (event) -> {
                boolean hit = hit(event.x, event.y);
                if(hit) onEvent(new MouseEnterEvent(event.x, event.y));
                else onEvent(new MouseExitEvent(event.x, event.y));
                return !hit; //stop propagation if not hit
            }, true);
            addHandler(MousePressedEvent.class, (event) -> {
                boolean hit = hit(event.x, event.y); // ensure mouse hits the Node
                if(hit && !isFocus()) {
                    onEvent(new FocusRequestEvent(this));
                }
                return !hit; //stop propagation if not hit
            }, true);
            addHandler(MouseReleasedEvent.class, (event) -> hit(event.x, event.y), true);

            /* Handles that manages Mouse Enter/Exit propagation */
            addHandler(MouseEnterEvent.class, (event) -> {
                boolean hovered = isHovered();
                setHovered(true);
                return hovered  || isLeaf(); //stop propagation if hovered
            }, true);
            addHandler(MouseExitEvent.class, (event) -> {
                boolean notHovered = !isHovered();
                setHovered(false);
                return notHovered || isLeaf(); //stop propagation if not hovered
            }, true);

            /* Handles that define the default key input listening logic */

            addHandler(KeyPressedEvent.class, (event) -> isFocus(), true); // ensure Node is Focussed
            addHandler(KeyReleasedEvent.class, (event) -> isFocus(), true); // ensure Node is Focussed
        }
    }

    public Node() {
        this(true);
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    private void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    private void setFocus(boolean focus) {
        this.focus = focus;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean hit(float x, float y) {
        Vec2 local = getLocalPosition(x, y);
        return (local.x >= 0 && local.x <= size.x && local.y >= 0 && local.y <= size.y);
    }

    /* Transformation Stuff */

    private void invalidateAbsoluteMatix() {
        absoluteInverseMatrixValid = false;
        //all children are cut from the absolute coords because of that
        for(Node child : children)
            child.invalidateAbsoluteMatix();
    }

    public Matrix3f getAbsoluteInverseMatrix() {
        if(!absoluteInverseMatrixValid) {
            if(parent == null) absoluteInverseMatrix.setIdentity();
            else absoluteInverseMatrix.set(parent.getAbsoluteInverseMatrix());
            absoluteInverseMatrix.multiply(transform.getInverse());
            absoluteInverseMatrixValid = true;
        }
        return absoluteInverseMatrix;
    }

    //uses the absoluteInverseMatrix to transform
    public Vec2 getLocalPosition(float x, float y) {
        return getAbsoluteInverseMatrix().multiply(new Vec2(x, y));
    }

    public Vec2 getSize() {
        return size;
    }

    public void setSize(float width, float height) {
        size.set(width, height);
    }

    public void translate(float x, float y) {
        invalidateAbsoluteMatix();
        transform.translate(x, y);
    }

    public void rotate(float angle) {
        invalidateAbsoluteMatix();
        transform.rotate(angle);
    }

    public void scale(float x, float y) {
        invalidateAbsoluteMatix();
        transform.scale(x, y);
    }

    /* Transformation Stuff End */

    public <T extends IEvent> void addHandler(Class<T> type, IEventHandler<T> handler, boolean capturePhase) {
        (capturePhase ? captureListener : bubbleListener).addEventHandler(type, handler);
    }

    public <T extends IEvent> void removeHandler(Class<T> type, IEventHandler<T> handler, boolean capturePhase) {
        (capturePhase ? captureListener : bubbleListener).removeEventHandler(type, handler);
    }

    public <T extends IEvent> void addHandler(Class<T> type, IEventHandler<T> handler) {
        addHandler(type, handler, false);
    }

    public <T extends IEvent> void removeHandler(Class<T> type, IEventHandler<T> handler) {
        removeHandler(type, handler, false);
    }

    public synchronized void add(Node node) {
        // already a child of this Node
        if(node.parent == this) return;
        // make sure the old parent removes this node first
        if(node.parent != null) node.parent.remove(node);
        children.add(node);
        node.parent = this;
        invalidateAbsoluteMatix(); //parent changed, invalidate the absolute matrix
    }

    public synchronized void remove(Node node) {
        // If node is succesfully removed of children, set it's node.parent to null
        if(children.remove(node)) {
            node.parent = null;
            invalidateAbsoluteMatix(); //parent changed, invalidate the absolute matrix
        }
    }

    //Bubble forwards to parent if unhandled
    private boolean bubble(IEvent event) {
        //if either this Node or it's parent handles event return true
        return bubbleListener.onEvent(event) || (parent != null && parent.bubble(event));
    }

    private boolean capture(IEvent event) {
        if(!visible) return false; // skip events (but don't consume) if invisible

        //If Event is captured, stop it
        if(captureListener.onEvent(event)) {
            return true;
        }

        if(children.size() > 0) {
            //Forward event to children, and make them try to capture it
            for (int i = children.size() - 1; i >= 0; i--)
                if (children.get(i).capture(event))
                    return true;
            return false;
        } else {
            //Start bubbling : there is no more children to forward
            return bubble(event);
        }
    }


    public final boolean onEvent(IEvent event) {
        return capture(event);
    }

    public void render(BatchGraphics g) {
        if(!visible) return;
        g.push(transform.getMatrix());
        onRender(g);
        for(Node child : children) {
            child.render(g);
        }
        g.pop();
    }

    protected abstract void onRender(BatchGraphics g);
}