package fr.hadriel.test.networkedECS;

import fr.hadriel.main.ecs.ECSEngine;
import fr.hadriel.main.ecs.events.ECSEvent;
import fr.hadriel.main.math.Mathf;
import fr.hadriel.main.math.Vec2;
import fr.hadriel.main.util.Timer;

import java.util.List;
import java.util.Random;

/**
 * Created by glathuiliere on 12/04/2017.
 */
public class SnapshotECSTest {

    private static final int WINDOW_SIZE = 200;
    private static final int MAX_SPEED = 10;

    private static void make2DEntity(ECSEngine engine, long id, Random random) {
        Vec2 position = new Vec2(Mathf.lerp(0, WINDOW_SIZE, random.nextFloat()), Mathf.lerp(0, WINDOW_SIZE, random.nextFloat()));
        Vec2 speed = new Vec2(Mathf.lerp(-MAX_SPEED, MAX_SPEED, random.nextFloat()), Mathf.lerp(-MAX_SPEED, MAX_SPEED, random.nextFloat()));
        engine.createEntity(id);
        engine.setComponent(id, "position", position);
        engine.setComponent(id, "speed", speed);

        System.out.println(String.format("Entity %d position=%s speed=%s", id, position, speed));
    }

    public static void main(String[] args) {
        MainWindow window1 = new MainWindow("Engine1", WINDOW_SIZE, WINDOW_SIZE);
        ECSEngine engine1 = new ECSEngine();
        engine1.addSystem(new MovementSystem());
        engine1.addSystem(new RenderSystem(window1.getCanvas()));

        Random random = new Random(0);
        for(int id = 0; id < 1000; id++) {
            make2DEntity(engine1, id, random);
        }

        MainWindow window2 = new MainWindow("Engine2", WINDOW_SIZE, WINDOW_SIZE);
        ECSEngine engine2 = new ECSEngine();
        engine2.addSystem(new MovementSystem());
        engine2.addSystem(new RenderSystem(window2.getCanvas()));

        List<ECSEvent> events = engine1.snapshot();
        engine2.submitAll(events);

        System.out.println("Press any key to start running");
        try { System.in.read(); } catch (Exception ignore) {}

        Timer timer = new Timer();
        while (!Thread.interrupted()) {
            float delta = timer.elapsed();
            timer.reset();
            engine1.update(delta);
            engine2.update(delta);
            try { Thread.sleep(16); } catch (Exception ignore) {}
        }
    }
}