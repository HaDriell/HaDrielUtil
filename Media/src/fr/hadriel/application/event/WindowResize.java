package fr.hadriel.application.event;

import fr.hadriel.event.IEvent;

/**
 * Fired when the window has been resized
 */
public class WindowResize implements IEvent {
    public final int width;
    public final int height;

    public WindowResize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
