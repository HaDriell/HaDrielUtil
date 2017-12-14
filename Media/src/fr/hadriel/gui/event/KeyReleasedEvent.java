package fr.hadriel.gui.event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyReleasedEvent extends UIEvent {

    public final int mods;
    public final int key;

    public KeyReleasedEvent(int key, int mods) {
        this.key = key;
        this.mods = mods;
    }
}
