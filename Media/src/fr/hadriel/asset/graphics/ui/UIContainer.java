package fr.hadriel.asset.graphics.ui;

import fr.hadriel.event.IEvent;
import fr.hadriel.renderers.BatchGraphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class UIContainer extends UIElement {

    private final List<UIElement> children;
    private boolean requireLayout;
    private boolean layoutLock;

    protected UIContainer() {
        this.children = new ArrayList<>();
    }

    public void add(UIElement element) {
        for (UIElement e = element; e != null; e = e.getParent())
            if (e == this) throw new IllegalArgumentException("An UIElement cannot be a child of itself");

        if (element.parent != null) {
            element.parent.remove(element);
        }
        children.add(element);
        element.parent = this;
    }

    public void invalidateLayout() {
        if (layoutLock) return; // ignore signals while layout is being setup. This way, user & system calls are both simple to implement & extend for UI dev
        requireLayout = true;
        super.invalidateLayout();
    }

    public void remove(UIElement element) {
        requireLayout = children.remove(element);
    }

    public Iterator<UIElement> children() {
        return new UIElementIterator(children, 0, children.size(), 1);
    }

    public Iterator<UIElement> childrenInverted() {
        return new UIElementIterator(children, children.size() - 1, -1, -1);
    }


    public UIElement capture(float x, float y) {
        if (hit(x, y)) { // hit means the Container contains the UIElement to capture
            Iterator<UIElement> it = childrenInverted();
            while (it.hasNext()) {
                UIElement element = it.next().capture(x, y);
                if (element != null) return element;
            }
        }
        return null;
    }

    @Override
    public IEvent onEvent(IEvent event) {
        // dispatch event to children
        Iterator<UIElement> it = childrenInverted();
        while (it.hasNext() && event != null) {
            event = it.next().onEvent(event);
        }
        return event;
    }

    public void render(BatchGraphics graphics) {
        if (requireLayout) {
            layoutLock = true;
            onLayout();
            layoutLock = false;
            requireLayout = false;
        }
        onRender(graphics);
        children.forEach(child -> child.render(graphics));
    }

    protected void onRender(BatchGraphics graphics) { } // default to NOP rendering
    protected abstract void onLayout();
}