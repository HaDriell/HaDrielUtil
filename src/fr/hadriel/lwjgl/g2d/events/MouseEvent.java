package fr.hadriel.lwjgl.g2d.events;

import fr.hadriel.event.Event;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 02/02/2017.
 */
public abstract class MouseEvent extends Event {


    public final float x;
    public final float y;

    protected MouseEvent(float x, float y) {
        super(true);
        this.x = x;
        this.y = y;
    }

    protected MouseEvent(Vec2 position) {
        super(true);
        this.x = position.x;
        this.y = position.y;
    }
}
