package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.Event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyPressedEvent extends Event {

    public final int mods;
    public final int key;

    public KeyPressedEvent(int key, int mods) {
        super(true);
        this.key = key;
        this.mods = mods;
    }
}