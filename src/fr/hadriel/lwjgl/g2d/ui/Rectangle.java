package fr.hadriel.lwjgl.g2d.ui;

import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.math.Vec2;

/**
 * Created by glathuiliere on 02/02/2017.
 */
public class Rectangle extends Node {

    private int hoveredColor;
    private int baseColor;

    public Rectangle(int width, int height, int baseColor, int hoveredColor) {
        setSize(width, height);
        this.hoveredColor = hoveredColor;
        this.baseColor = baseColor;
    }

    protected void onRender(BatchGraphics g) {
        if(isHovered()) g.setColor(hoveredColor);
        else g.setColor(baseColor);
        Vec2 size = getSize();
        g.fillRect(0, 0, size.x, size.y);
    }

    public String toString() {
        return "R(" + hoveredColor + ")";
    }
}
