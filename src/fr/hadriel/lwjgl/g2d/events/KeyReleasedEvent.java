package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.Event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyReleasedEvent extends Event {

    public final int key;

    public KeyReleasedEvent(int key) {
        super(true);
        this.key = key;
    }
}
