package fr.hadriel.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere setOn 08/08/2016.
 */
public class MousePressedEvent implements IEvent {

    public float x, y;
    public int button;

    public MousePressedEvent(float x, float y, int button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }

    public MousePressedEvent(Vec2 mouse, int button) {
        this(mouse.x, mouse.y, button);
    }
}
