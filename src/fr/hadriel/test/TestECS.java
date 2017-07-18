package fr.hadriel.test;

import fr.hadriel.main.ecs.ECSEngine;
import fr.hadriel.main.ecs.EntityDataManager;
import fr.hadriel.main.ecs.EntitySystem;
import fr.hadriel.main.ecs.filter.Filters;
import fr.hadriel.main.math.Mathf;
import fr.hadriel.main.math.Vec2;
import fr.hadriel.main.util.Timer;

/**
 * Created by glathuiliere on 31/03/2017.
 */
public class TestECS {

    private static class MovementSystem extends EntitySystem {

        private int count;
        private Timer timer;

        public MovementSystem() {
            super(
                    Filters.Require("position", Vec2.class),
                    Filters.Require("speed", Vec2.class)
            );
        }

        protected void begin() {
            count = 0;
            timer = new Timer();
        }

        protected void update(EntityDataManager manager, long id, float delta) {
            Vec2 position = manager.getComponent(id, "position", Vec2.class);
            Vec2 speed = manager.getComponent(id, "speed", Vec2.class);
            position.x += speed.x * delta;
            position.y += speed.y * delta;
            if(count == 3) System.out.println("Entity 3 position:" + position);
            count++;
        }

        protected void end() {
            System.out.println(String.format("Processed %d Entities in %.2f ms", count, timer.elapsed() * 1000));
        }
    }


    private static final int ENTITY_COUNT = 100_000;
    public static void main(String[] args) {
        ECSEngine engine = new ECSEngine();
        engine.addSystem(new MovementSystem());

        System.out.println("Creating " + ENTITY_COUNT + " Entities");
        for(int id = 0; id < ENTITY_COUNT; id++) {
            engine.createEntity(id);
            engine.setComponent(id, "position", new Vec2(Mathf.random(), Mathf.random()));
            if(Mathf.random() > 0.1f) {
                engine.setComponent(id, "speed", new Vec2(Mathf.random(), Mathf.random()));
            }
        }
        System.out.println("Creation Done");

        Timer timer = new Timer();
        while(!Thread.interrupted()) {
            float delta = timer.elapsed();
            timer.reset();
            engine.update(delta);

            int ms = (int) (timer.elapsed() * 1000);
            if(ms < 16) try { Thread.sleep(16 - ms); } catch (InterruptedException ignore) {}
        }
    }
}
