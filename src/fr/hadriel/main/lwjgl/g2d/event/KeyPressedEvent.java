package fr.hadriel.main.lwjgl.g2d.event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class KeyPressedEvent extends UIEvent {

    public final int mods;
    public final int key;

    public KeyPressedEvent(int key, int mods) {
        this.key = key;
        this.mods = mods;
    }
}