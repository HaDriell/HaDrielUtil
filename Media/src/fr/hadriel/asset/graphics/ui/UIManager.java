package fr.hadriel.asset.graphics.ui;


import fr.hadriel.application.event.*;
import fr.hadriel.asset.graphics.ui.containers.AnchorPane;
import fr.hadriel.event.IEvent;

import java.util.Iterator;

public class UIManager extends AnchorPane {

    private UIElement hover; // element that is hovered by the mouse
    private UIElement focus; // element that owns the focus

    public UIElement getHover() {
        return hover;
    }

    public UIElement getFocus() {
        return focus;
    }

    public void requestFocus(UIElement element) {
        if (focus != null) focus.focusLost(new FocusLostEvent());
        focus = element;
        if (focus != null) focus.focusGained(new FocusGainedEvent());
    }

    private void computeHover(float x, float y) {
        UIElement element = capture(x, y);
        if (element == hover) return;
        if (hover != null) hover.onEvent(new MouseLeftEvent());
        hover = element;
        if (hover != null) hover.onEvent(new MouseEnteredEvent());
    }

    public IEvent onEvent(IEvent event) {
        if (event instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) event;
            computeHover(me.x, me.y);
        }

        Iterator<UIElement> it = childrenInverted();
        while (it.hasNext() && event != null) {
            event = it.next().onEvent(event);
        }
        return event;
    }
}