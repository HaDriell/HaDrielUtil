package fr.hadriel;

import fr.hadriel.events.input.MouseMovedEvent;
import fr.hadriel.graphics.Window;
import fr.hadriel.graphics.geom.PolygonDebug;
import fr.hadriel.graphics.ui.DirectDraw;
import fr.hadriel.graphics.ui.Group;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geom.Circle;
import fr.hadriel.math.geom.Polygon;
import fr.hadriel.math.Collision;

import java.awt.*;

/**
 * Created by glathuiliere on 05/09/2016.
 */
public class TestMath {

    public static final Polygon BOX = new Polygon(
            new Vec2(-1, -1),
            new Vec2(+1, -1),
            new Vec2(+1, +1),
            new Vec2(-1, +1)
    );

    public static final Circle CIRCLE = new Circle(1f, 3);

    public static void main(String[] args) {
        Window w = new Window();

        PolygonDebug A = new PolygonDebug(CIRCLE, null, Color.white);
        PolygonDebug B = new PolygonDebug(BOX, null, Color.gray);

        A.scale.set(15, 15);
        B.scale.set(10, 50);
        B.position.set(100, 100);


        DirectDraw directDraw = new DirectDraw(w.getSize().width, w.getSize().height) {
            public boolean onMouseMoved(MouseMovedEvent event) {
                A.position.set(event.x, event.y);
                Matrix3f ma = A.getMatrix();
                Matrix3f mb = B.getMatrix();
                Polygon pa = A.polygon.copy().transform(ma);
                Polygon pb = B.polygon.copy().transform(mb);
                B.fillColor = Collision.polygonToPolygon(pa, pb) ? Color.darkGray : Color.lightGray;
                return true;
            }
        };


        directDraw.add(B);
        directDraw.add(A);

        Group g = new Group();
        g.add(directDraw);
        w.addRoot(g);
        w.start();
    }
}
