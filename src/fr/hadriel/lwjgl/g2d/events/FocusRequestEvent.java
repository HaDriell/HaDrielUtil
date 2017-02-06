package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.Event;
import fr.hadriel.event.Node;

/**
 * Created by glathuiliere on 02/02/2017.
 */
public class FocusRequestEvent extends Event {

    public final Node node;

    public FocusRequestEvent(Node node) {
        super(true);
        this.node = node;
    }
}