package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 08/08/2016.
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
