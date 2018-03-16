package fr.hadriel.math.geometry;

import fr.hadriel.math.Vec2;

public class Circle {
    public final float radius;
    public final Vec2 center;

    public Circle(Vec2 center, float radius) {
        this.center = center;
        this.radius = radius;
    }
}