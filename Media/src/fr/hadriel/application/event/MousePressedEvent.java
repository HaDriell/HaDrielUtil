package fr.hadriel.application.event;

import fr.hadriel.math.Vec2;

/**
 * Fired on a widget when mouse button is pressed.
 * Fired on the focus if there is a Focused Component.
 */
public class MousePressedEvent extends MouseEvent {

    public final int button;

    public MousePressedEvent(float x, float y, int button) {
        super(x, y);
        this.button = button;
    }

    public MousePressedEvent(Vec2 mouse, int button) {
        super(mouse);
        this.button = button;
    }
}
