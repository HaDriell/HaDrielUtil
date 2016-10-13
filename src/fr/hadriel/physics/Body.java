package fr.hadriel.physics;

import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaDriel on 09/09/2016.
 */
public class Body {

    public final BodyProperties properties = new BodyProperties();
    private List<Polygon> polygons = new ArrayList<>();

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public void removePolygon(Polygon polygon) {
        polygons.remove(polygon);
    }

    public Polygon[] getTransformedPolygons() {
        Polygon[] array = new Polygon[polygons.size()];
        Matrix3f matrix = properties.getMatrix();
        for(int i = 0; i < array.length; i++) {
            array[i] = polygons.get(i).copy().transform(matrix);
        }
        return array;
    }
}