package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 02/02/2017.
 */
public class MouseEnterEvent extends MouseEvent {

    public MouseEnterEvent(float x, float y) {
        super(x, y);
    }

    public MouseEnterEvent(Vec2 position) {
        super(position);
    }
}
