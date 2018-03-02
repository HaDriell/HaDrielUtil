package fr.hadriel.window.event;

import fr.hadriel.event.IEvent;

/**
 * Fired on a widget when a key is released.
 * Fired on the Focus OR on the root
 */
public class KeyReleasedEvent implements IEvent {

    public final int mods;
    public final int key;

    public KeyReleasedEvent(int key, int mods) {
        this.key = key;
        this.mods = mods;
    }
}
