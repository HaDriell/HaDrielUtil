package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyReleasedEvent implements IEvent {

    public final int key;
    public final int mods;

    public KeyReleasedEvent(int key, int mods) {
        this.key = key;
        this.mods = mods;
    }
}
