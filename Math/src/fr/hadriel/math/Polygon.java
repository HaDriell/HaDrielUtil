package fr.hadriel.math;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public class Polygon {

    private List<Vec2> vertices;

    public Polygon(List<Vec2> vertices) {
        this.vertices = Objects.requireNonNull(vertices);
        if(vertices.size() < 3) throw new IllegalArgumentException("less than 3 vertices in polygon");
    }
}