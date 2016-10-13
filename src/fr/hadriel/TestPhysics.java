package fr.hadriel;

import fr.hadriel.graphics.Window;
import fr.hadriel.graphics.ui.DirectDraw;
import fr.hadriel.graphics.ui.Group;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geom.Circle;
import fr.hadriel.math.geom.Polygon;
import fr.hadriel.physics.Body;
import fr.hadriel.physics.PhysicsEngine;
import fr.hadriel.physics.PhysicsEngineDebug;
import fr.hadriel.time.Timer;


/**
 * Created by glathuiliere on 05/09/2016.
 */
public class TestPhysics {

    public static void main(String[] args) {
        //Engine
        PhysicsEngine engine = new PhysicsEngine();
        Thread daemon = new Thread(() -> {
            Timer t = new Timer();
            while (!Thread.interrupted()) {
                engine.update(t.elapsed());
                t.reset();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ignore) {}
            }
        });
        daemon.setDaemon(true);
        daemon.start();

        //Data
        Polygon CIRCLE = new Circle(6f, 12);

        Body b1 = new Body();
        Body b2 = new Body();

        b1.addPolygon(CIRCLE);
        b1.properties.position.set(100, 100);
        b1.properties.acceleration.set(10, 0);

        b2.addPolygon(CIRCLE);
        b2.properties.position.set(150, 100);
        b2.properties.acceleration.set(0, 0);

        engine.addBody(b1);
        engine.addBody(b2);

        //GUI
        Window w = new Window();
        PhysicsEngineDebug debug = new PhysicsEngineDebug(engine);
        DirectDraw directDraw = new DirectDraw(w.getSize().width, w.getSize().height);
        directDraw.add(debug);
        Group g = new Group();
        g.add(directDraw);
        w.addRoot(g);
        w.start();
    }
}
