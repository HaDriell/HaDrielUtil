package fr.hadriel;

import fr.hadriel.ecs.World;
import fr.hadriel.ecs.processors.MovementProcessor;
import fr.hadriel.util.Timer;

/**
 * Created by glathuiliere on 09/01/2017.
 */
public class TestNetworkedECS {

    public static void main(String[] args) {
    }

    private static World createManagedEntityWorld() {
        World world = new World();
        world.addProcessor(new MovementProcessor());
        new Thread(() -> {
            Timer t = new Timer();
            while (!Thread.interrupted()) {
                world.update(t.elapsed());
                t.reset();
                try { Thread.sleep(16); } catch (Exception ignore) {}
            }
        }, "WorldThread").start();
        return world;
    }

    public static void startServerWorld(int port) {
        World world = createManagedEntityWorld();
        new Thread(() -> {
        }).start();
    }
}