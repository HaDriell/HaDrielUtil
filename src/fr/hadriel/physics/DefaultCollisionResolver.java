package fr.hadriel.physics;

import fr.hadriel.math.Vec2;
import fr.hadriel.math.geom.Polygon;

/**
 * Created by HaDriel on 10/09/2016.
 */
public class DefaultCollisionResolver implements CollisionResolver {
    public void resolve(Body a, Body b) {
        Polygon[] aPolygons = a.getTransformedPolygons();
        Polygon[] bPolygons = b.getTransformedPolygons();

        for(Polygon A : aPolygons) {
            for(Polygon B : bPolygons) {

            }
        }
        //TODO : compute angular speed (new Vec2) and add linear velocity
        Vec2 avv = a.properties.velocity;
        Vec2 bvv = b.properties.velocity;
    }
}
