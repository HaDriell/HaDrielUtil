package fr.hadriel.main.lwjgl.g2d.event;

import fr.hadriel.main.lwjgl.g2d.ui.Widget;

/**
 * Created by glathuiliere on 08/02/2017.
 */
public class FocusChangedEvent extends UIEvent {

    public final Widget widget;

    public FocusChangedEvent(Widget widget) {
        this.widget = widget;
    }
}