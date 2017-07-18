package fr.hadriel.test.networkedECS;

import fr.hadriel.main.ecs.ECSEngine;
import fr.hadriel.main.math.Vec2;
import fr.hadriel.main.util.Timer;

/**
 * Created by glathuiliere on 10/04/2017.
 */
public class MainClient {

    public static void main(String[] args) {

        MainWindow window = new MainWindow(200, 200);
        ECSEngine engine = new ECSEngine();
        PlayerController control = new PlayerController(1);
        window.bind(control);

        //Configure systems (render + gameplay)
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(window.getCanvas()));

        //Create entity
        engine.createEntity(control.id);
        engine.setComponent(control.id, "position", new Vec2(100, 100));
        engine.setComponent(control.id, "speed", new Vec2(2f, 1f));

        Timer timer = new Timer();
        while (!Thread.interrupted()) {
            float delta = timer.elapsed();
            timer.reset();
            engine.update(delta);

            //playerinput update
            engine.setComponent(control.id, "speed", control.getSpeed());

            try { Thread.sleep(16); } catch (Exception ignore) {}
        }
    }
}
