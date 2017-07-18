package fr.hadriel.main.lwjgl.g2d.event;

import fr.hadriel.main.math.Vec2;

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