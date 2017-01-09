package fr.hadriel.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere setOn 08/08/2016.
 */
public class MouseMovedEvent implements IEvent {

    public final float x, y;
    public final boolean dragged;

    public MouseMovedEvent(float x, float y, boolean dragged) {
        this.x = x;
        this.y = y;
        this.dragged = dragged;
    }

    public MouseMovedEvent(Vec2 mouse, boolean dragged) {
        this(mouse.x, mouse.y, dragged);
    }
}