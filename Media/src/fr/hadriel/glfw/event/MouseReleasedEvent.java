package fr.hadriel.glfw.event;

import fr.hadriel.math.Vec2;

/**
 * Fired on a widget when mouse button is released.
 * Fired on the focus if there is a Focused Component.
 */
public class MouseReleasedEvent extends MouseEvent {

    public final int button;

    public MouseReleasedEvent(float x, float y, int button) {
        super(x, y);
        this.button = button;
    }

    public MouseReleasedEvent(Vec2 position, int button) {
        super(position);
        this.button = button;
    }
}