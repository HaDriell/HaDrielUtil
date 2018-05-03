package fr.hadriel.asset.graphics.gui;

import fr.hadriel.event.IEvent;
import fr.hadriel.renderers.BatchGraphics;

import java.util.ArrayList;
import java.util.List;

public class UIContainer extends UIElement {

    private List<UIElement> children;

    public UIContainer() {
        this.children = new ArrayList<>();
    }

    public void add(UIElement element) {
        if (children.contains(element)) return;
        children.add(element);
        element.setParent(this);
    }

    public void remove(UIElement element) {
        if (children.remove(element)) {
            element.setParent(null);
            //TODO signal layout update on this
        }
    }

    @Override
    public IEvent onEvent(IEvent event) {
        for (int i = children.size() - 1; i >= 0; i--) {
            event = children.get(i).onEvent(event);
        }
        return event;
    }

    protected void onRender(BatchGraphics graphics, float width, float height) {
        children.forEach(child -> child.render(graphics));
    }
}