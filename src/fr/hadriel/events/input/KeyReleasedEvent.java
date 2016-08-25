package fr.hadriel.events.input;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyReleasedEvent implements IEvent {

    public int key;

    public KeyReleasedEvent(int key) {
        this.key = key;
    }
}
