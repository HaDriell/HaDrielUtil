package fr.hadriel.math.geometry;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geometry.Epsilon;
import fr.hadriel.math.geometry.Geometry;

/**
 *
 * @author glathuiliere
 */
public class Polygon {

    public final Vec2[] vertices;
    public final Vec2[] normals;

    public Polygon(Vec2[] vertices, Vec2[] normals) {
        this.vertices = vertices;
        this.normals = normals;
    }

    public Polygon(Vec2... vertices) {
        this.vertices = vertices;
        this.normals = Geometry.getCounterClockwiseEdgeNormals(vertices);
    }
}