package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.lwjgl.g2d.ui.Node;

/**
 * Created by glathuiliere on 02/02/2017.
 */
public class FocusRequestEvent implements IEvent {

    public final Node node;

    public FocusRequestEvent(Node node) {
        this.node = node;
    }
}