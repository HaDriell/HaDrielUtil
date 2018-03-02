package fr.hadriel.window.event;

import fr.hadriel.event.IEvent;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 02/02/2017.
 *
 * Holds the absolute position of the cursor in the Window
 */
public abstract class MouseEvent implements IEvent {


    public final float x;
    public final float y;

    protected MouseEvent(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    protected MouseEvent(Vec2 position) {
        this.x = position.x;
        this.y = position.y;
    }
}
