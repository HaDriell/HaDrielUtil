package fr.hadriel.asset.graphics.gui;


import fr.hadriel.application.event.*;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.renderers.BatchGraphics;

import java.util.List;

public class UIManager implements IEventListener {

    private List<UIElement> elements; // list of all elements

    private UIElement hover; // element that is hovered by the mouse
    private UIElement focus; // element that owns the focus

    public void requestFocus(UIElement element) {
        if (focus != null) focus.focusLost(new FocusLostEvent());
        focus = element;
        if (focus != null) focus.focusGained(new FocusGainedEvent());
    }

    //Updates the Hover on mouse movement
    private void capture() {

    }

    public UIElement getCapturedUIElement(float x, float y) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            UIElement element = elements.get(i);
            if (element.hit(x, y)) {
                return element;
            }
        }
        return null;
    }

    public IEvent onEvent(IEvent event) {
        int it = elements.size();
        while (it-- > 0 && event != null) {
            UIElement element = elements.get(it);

        }
        return event;
    }

    public void render(BatchGraphics graphics) {
        for (UIElement element : elements) {
            if (element.enabled)
                element.render(graphics);
        }
    }
}