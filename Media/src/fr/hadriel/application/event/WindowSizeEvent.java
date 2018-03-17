package fr.hadriel.application.event;

import fr.hadriel.event.IEvent;

public class WindowSizeEvent implements IEvent {
    public final int width;
    public final int height;

    public WindowSizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
