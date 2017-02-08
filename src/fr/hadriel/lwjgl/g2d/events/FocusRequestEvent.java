package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.Event;
import fr.hadriel.lwjgl.g2d.ui.Widget;

/**
 * Created by glathuiliere on 08/02/2017.
 */
public class FocusRequestEvent extends Event {

    public final Widget widget;

    public FocusRequestEvent(Widget widget) {
        super(true);
        this.widget = widget;
    }
}