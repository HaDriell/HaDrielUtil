package fr.hadriel.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class MouseMovedEvent implements IEvent {

    public float x, y;
    public boolean dragged;

    public MouseMovedEvent(float x, float y, boolean dragged) {
        this.x = x;
        this.y = y;
        this.dragged = dragged;
    }
}