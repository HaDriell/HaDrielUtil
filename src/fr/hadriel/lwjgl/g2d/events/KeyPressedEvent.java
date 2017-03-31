package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyPressedEvent implements IEvent {

    public final int mods;
    public final int key;

    public KeyPressedEvent(int key, int mods) {
        this.key = key;
        this.mods = mods;
    }
}