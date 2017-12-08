package fr.hadriel.graphics.g2d.event;

import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 08/08/2016.
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