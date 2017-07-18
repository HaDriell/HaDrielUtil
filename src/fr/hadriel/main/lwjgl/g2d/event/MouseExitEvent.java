package fr.hadriel.main.lwjgl.g2d.event;

import fr.hadriel.main.math.Vec2;

/**
 * Created by glathuiliere on 02/02/2017.
 */
public class MouseExitEvent extends MouseEvent {

    public MouseExitEvent(float x, float y) {
        super(x, y);
    }

    public MouseExitEvent(Vec2 position) {
        super(position);
    }
}
