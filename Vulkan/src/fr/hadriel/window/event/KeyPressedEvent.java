package fr.hadriel.window.event;

import fr.hadriel.event.IEvent;

/**
 * Fired on a widget when a key is pressed.
 * Fired on the Focus OR on the root
 */
public class KeyPressedEvent implements IEvent {

    public final int mods;
    public final int key;

    public KeyPressedEvent(int key, int mods) {
        this.key = key;
        this.mods = mods;
    }
}