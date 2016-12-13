package fr.hadriel.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyPressedEvent implements IEvent {

    public int key;

    public KeyPressedEvent(int key) {
        this.key = key;
    }
}