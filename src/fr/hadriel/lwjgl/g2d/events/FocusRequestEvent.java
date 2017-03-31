package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.lwjgl.g2d.ui.Widget;

/**
 * Created by glathuiliere on 08/02/2017.
 */
public class FocusRequestEvent implements IEvent {

    public final Widget widget;

    public FocusRequestEvent(Widget widget) {
        this.widget = widget;
    }
}