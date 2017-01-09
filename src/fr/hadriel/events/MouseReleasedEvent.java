package fr.hadriel.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere setOn 08/08/2016.
 */
public class MouseReleasedEvent implements IEvent {

    public final float x, y;
    public final int button;

    public MouseReleasedEvent(float x, float y, int button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }

    public MouseReleasedEvent(Vec2 mouse, int button) {
        this(mouse.x, mouse.y, button);
    }
}
