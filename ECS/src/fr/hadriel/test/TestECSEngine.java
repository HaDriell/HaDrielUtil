package fr.hadriel.test;

import fr.hadriel.ecs.ECSEngine;
import fr.hadriel.ecs.EntitySystem;
import fr.hadriel.ecs.IEntityManager;
import fr.hadriel.ecs.Profile;

/**
 * Created by gauti on 20/12/2017.
 */
public class TestECSEngine {
    public static strictfp final class Speed {
        public float x, y;
    }

    public static strictfp final class Position {
        public float x, y;
    }

    public static class MovementSystem extends EntitySystem {

        public MovementSystem() {
            super(Profile.include(Speed.class, Position.class));
        }

        public void process(long id, IEntityManager manager, float deltaTime) {
            Speed s = manager.getComponent(id, Speed.class);
            Position p = manager.getComponent(id, Position.class);
            Position dp = new Position();
            dp.x = p.x + s.x * deltaTime;
            dp.y = p.y + s.y * deltaTime;

            manager.setComponent(id, dp);
        }
        public void onEntityRegistered(long id) {} // don't care
        public void begin() {} // don't care
        public void end() {} // don't care
        public void onEntityUnregistered(long id) {} // don't care
    }

    public static long createMovableEntity(ECSEngine engine, float sx, float sy) {
        long id = engine.entities().create();

        Speed speed = new Speed();
        speed.x = sx;
        speed.y = sy;

        Position position = new Position();
        position.x = 0;
        position.y = 0;

        engine.entities().setComponent(id, speed);
        engine.entities().setComponent(id, position);
        return id;
    }

    public static void main(String[] args) {
        ECSEngine engine = new ECSEngine();
        engine.addEntitySystem(new MovementSystem());
        createMovableEntity(engine, 0, 0);
        long id = createMovableEntity(engine, 1, 0);
        createMovableEntity(engine, 0, 1);

        Timer timer = new Timer();
        float P_X = 0;
        while(!Thread.interrupted() && P_X < 1) { // won't fucking wait too much on this
            P_X = engine.entities().getComponent(id, Position.class).x;
//            System.out.println("P_X=" + P_X);
            float dt = timer.elapsed();
            timer.reset();

            engine.update(dt);
            try {Thread.sleep(50);} catch (InterruptedException ignore) {}
        }
    }

    private static final class Timer {
        private long anchor = System.nanoTime();

        public float elapsed() {
            return (System.nanoTime() - anchor) / 1_000_000_000F;
        }

        public void reset() {
            anchor = System.nanoTime();
        }
    }
}
