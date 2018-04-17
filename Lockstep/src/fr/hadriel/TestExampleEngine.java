package fr.hadriel;

import fr.hadriel.lockstep.*;
import fr.hadriel.util.Timer;

import java.util.Optional;
import java.util.Random;

public class TestExampleEngine {

    private static class Position implements IComponent {
        public final float x;
        public final float y;

        public Position(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float len2() {
            return x*x + y*y;
        }

        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }
    }

    private static class Speed implements IComponent {
        public final float x;
        public final float y;

        public Speed(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }
    }

    private static class Entity2DMovement extends EntityProcessor {

        public Entity2DMovement() {
            super(EntityProfile.include(Position.class, Speed.class));
        }

        protected void process(Entity entity, float deltaTime) {
            Position position = entity.getComponent(Position.class);
            Speed speed = entity.getComponent(Speed.class);

            float px = position.x + speed.x * deltaTime;
            float py = position.y + speed.y * deltaTime;

            entity.setComponent(new Position(px, py));
        }

        protected void begin(float deltaTime) { }
        protected void end(float deltaTime) { }
        public void load() { }
        public void unload() { }
    }

    public static void main(String[] args) {
        LockstepEngine engine = new LockstepEngine();
        engine.addSystem(new Entity2DMovement());

        Position p = new Position(0, 0);
        Random r = new Random(0);

        System.out.println("Generating Entities");
        for(int i = 0; i < 100_000; i++) {
            engine.newEntity(p, new Speed(r.nextFloat(), r.nextFloat()));
            switch (i) {
                case 10_000:
                case 20_000:
                case 30_000:
                case 40_000:
                case 50_000:
                case 60_000:
                case 70_000:
                case 80_000:
                case 90_000:
                case 99_999:
                    System.out.println(String.format("Please wait, loading (%d%%)", i / 1_000));
            }
        }

        System.out.println("Starting Simulation");
        Timer timer = new Timer();
        for(int i = 0; i < 10; i++) {
            timer.reset();
            engine.step(10f);
            System.out.println("Step processed in " + timer.elapsed() * 1000f + " ms");
        }
        System.out.println("Ending Simulation");


        Entity e = engine.entities(EntityProfile.include(Position.class))
                .sorted((a, b) -> Float.compare(a.getComponent(Position.class).len2(), b.getComponent(Position.class).len2()))
                .findFirst().get();
        System.out.println(e);
    }
}