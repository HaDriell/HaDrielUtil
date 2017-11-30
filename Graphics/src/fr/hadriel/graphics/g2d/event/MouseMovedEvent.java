package fr.hadriel.graphics.g2d.event;

import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 08/08/2016.
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