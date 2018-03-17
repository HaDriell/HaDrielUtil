package fr.hadriel.application.event;

import fr.hadriel.math.Vec2;

/**
 * Fired on a widget when mouse movement is detected on a Component
 */
public class MouseMovedEvent extends MouseEvent {

    public MouseMovedEvent(float x, float y) {
        super(x, y);
    }

    public MouseMovedEvent(Vec2 mouse) {
        super(mouse);
    }
}