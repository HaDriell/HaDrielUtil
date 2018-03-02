package fr.hadriel.application.event;

import fr.hadriel.math.Vec2;

/**
 * Fired on a widget when mouse movement is detected on a Component
 */
public class MouseMovedEvent extends MouseEvent {

    public final boolean dragged;

    public MouseMovedEvent(float x, float y, boolean dragged) {
        super(x, y);
        this.dragged = dragged;
    }

    public MouseMovedEvent(Vec2 mouse, boolean dragged) {
        super(mouse);
        this.dragged = dragged;
    }
}