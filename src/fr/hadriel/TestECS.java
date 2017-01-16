package fr.hadriel;

import fr.hadriel.entities.Entity;
import fr.hadriel.entities.EntityProcessor;
import fr.hadriel.entities.World;
import fr.hadriel.math.Vec2;
import fr.hadriel.util.Timer;


/**
 * Created by glathuiliere on 09/01/2017.
 */
public class TestECS {

    public static final String POSITION = "position";
    public static final String SPEED = "speed";

    public static void main(String[] args) {
        World world = new World();



        //Mover processor
        world.addProcessor(new EntityProcessor() {
            public void beforeUpdate() {}
            public void update(World world, Entity entity, float delta) {
                Vec2 position = entity.get(POSITION, Vec2.class);
                Vec2 speed = entity.get(SPEED, Vec2.class);

                if(position == null || speed == null) {
                    System.out.println("error");
                    return;
                }
                position.add(speed.x * delta, speed.y * delta);
                entity.set(POSITION, position);
                System.out.println("Processed Entity " + entity.id + " POSITION=" + position);
            }
            public void afterUpdate() {}
        });
        new Thread(() -> {
            Timer t = new Timer();
            while(!Thread.interrupted()) {
                float dt = t.elapsed();
                t.reset();
                world.pollEvents();
                world.update(dt);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ignore) {}
            }
        }).start();

        world.createEntity(1);
        world.setProperty(1, POSITION, new Vec2(0, 0));
        world.setProperty(1, SPEED, new Vec2(1, 0));
    }
}