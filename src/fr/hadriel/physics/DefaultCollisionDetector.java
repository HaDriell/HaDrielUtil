package fr.hadriel.physics;


import fr.hadriel.math.Collisions;
import fr.hadriel.math.geom.Polygon;

/**
 * Created by HaDriel on 10/09/2016.
 */
public class DefaultCollisionDetector implements CollisionDetector {

    public boolean collides(Body a, Body b) {
        Polygon[] aArray = a.getTransformedPolygons();
        Polygon[] bArray = b.getTransformedPolygons();
        for(Polygon A : aArray) {
            for(Polygon B : bArray) {
                if(Collisions.polygonToPolygon(A, B)) {
                    return true;
                }
            }
        }
        return false;
    }
}
