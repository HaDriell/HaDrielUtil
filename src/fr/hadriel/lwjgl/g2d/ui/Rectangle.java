package fr.hadriel.lwjgl.g2d.ui;

import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.g2d.events.MouseMovedEvent;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 07/02/2017.
 */
public class Rectangle extends Widget {

    private int onHoveredColor  = 0xFFFFFFFF;
    private int onIdleColor     = 0x000000FF;
    private int onFocusColor    = 0xAAAAAAFF;
    private Vec2 cursor = new Vec2();

    public Rectangle(float width, float height) {
        setSize(width, height);
        setFocusable(true);
        addEventHandler(MouseMovedEvent.class, (event) -> cursor.set(getAbsoluteInverse().multiply(new Vec2(event.x, event.y))));
    }

    public void setOnFocusColor(int onFocusColor) {
        this.onFocusColor = onFocusColor;
    }

    public void setOnHoveredColor(int onHoveredColor) {
        this.onHoveredColor = onHoveredColor;
    }

    public void setOnIdleColor(int onIdleColor) {
        this.onIdleColor = onIdleColor;
    }

    protected void onRender(BatchGraphics g) {
        g.setColor(isFocused() ? onFocusColor : onIdleColor);
        if(isHovered()) g.setColor(onHoveredColor);
        g.fillRect(0, 0, size.x, size.y);

        g.setColor(isHovered() ? onIdleColor : onHoveredColor);
        g.drawRect(0, 0, size.x, size.y);

        g.push(Matrix3f.Translation(cursor.x, cursor.y));
        g.setColor(isHovered() ? onIdleColor : onHoveredColor);
        g.fillRect(-5, -5, 10, 10);
        g.pop();
    }
}