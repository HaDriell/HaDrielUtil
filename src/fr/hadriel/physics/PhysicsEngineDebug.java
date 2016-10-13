package fr.hadriel.physics;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.HLRenderable;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geom.Polygon;

import java.awt.*;
import java.util.List;

/**
 * Created by HaDriel on 09/09/2016.
 */
public class PhysicsEngineDebug implements HLRenderable {

    private PhysicsEngine engine;

    public PhysicsEngineDebug(PhysicsEngine engine) {
        this.engine = engine;
    }

    public void render(HLGraphics g) {
        Body[] bodies = engine.getBodies();
        for(Body body : bodies) {
            for(Polygon polygon : body.getTransformedPolygons()) {
                Vec2[] vertices = polygon.getVertices();
                int count = vertices.length;
                int[] x = new int[count];
                int[] y = new int[count];
                for(int i = 0; i < count; i++) {
                    x[i] = (int) vertices[i].x;
                    y[i] = (int) vertices[i].y;
                }
                g.drawPolygon(x, y, count, Color.yellow);
            }
        }
    }
}
