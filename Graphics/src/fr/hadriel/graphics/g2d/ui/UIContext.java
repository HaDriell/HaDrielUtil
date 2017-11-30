package fr.hadriel.graphics.g2d.ui;

import fr.hadriel.graphics.g2d.event.FocusGainEvent;
import fr.hadriel.graphics.g2d.event.FocusLostEvent;
import fr.hadriel.graphics.g2d.event.MouseEnterEvent;
import fr.hadriel.graphics.g2d.event.MouseExitEvent;

/**
 * Created by glathuiliere on 19/07/2017.
 */
public class UIContext {

    /* Holds the reference of the focused widget */
    private Widget focused;

    /* Holds the reference of the hovered widget */
    private Widget hovered;

    public UIContext() {
        this.focused = null;
        this.hovered = null;
    }

    public void setHovered(Widget widget) {
        if(hovered == widget) return; //spamming protection
        //Transition between hovered and widget
        if(hovered != null) hovered.onEvent(new MouseExitEvent());
        hovered = widget;
        if(hovered != null) hovered.onEvent(new MouseEnterEvent());
    }

    public void setFocused(Widget widget) {
        if(focused == widget) return; //spamming protection
        //Transition between focused and widget
        if(focused != null) focused.onEvent(new FocusLostEvent());
        focused = widget;
        if(focused != null) focused.onEvent(new FocusGainEvent());
    }

    public boolean isHovered(Widget widget) {
        return hovered == widget;
    }

    public boolean isFocused(Widget widget) {
        return focused == widget;
    }
}