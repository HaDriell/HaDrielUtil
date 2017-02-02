package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyPressedEvent implements IEvent {

    public final int key;

    public KeyPressedEvent(int key) {
        this.key = key;
    }
}
