package fr.hadriel.window.event;

import fr.hadriel.event.IEvent;

/**
 * Fired when the window has been resized
 */
public class WindowResizeEvent implements IEvent {
    public final int width;
    public final int height;

    public WindowResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
