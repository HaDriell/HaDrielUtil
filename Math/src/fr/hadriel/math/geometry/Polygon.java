package fr.hadriel.math.geometry;

import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Convex> decompose() {
        return Bayazit.decompose(vertices);
    }

    public List<Triangle> triangulate() {
        List<Triangle> triangles = new ArrayList<>();
        decompose().forEach(convex -> triangles.addAll(convex.triangulate()));
        return triangles;
    }
}