package fr.hadriel;

import fr.hadriel.ecs.Entity;
import fr.hadriel.ecs.World;
import fr.hadriel.ecs.components.AccelerationComponent;
import fr.hadriel.ecs.components.LocationComponent;
import fr.hadriel.ecs.components.SpeedComponent;
import fr.hadriel.ecs.processors.MovementProcessor;
import fr.hadriel.serialization.struct.StObject;
import fr.hadriel.util.Timer;

/**
 * Created by glathuiliere on 09/01/2017.
 */
public class TestECSRecplication {

    public static void main(String[] args) {
        World world = new World();
        world.addProcessor(new MovementProcessor());

        Entity d = new Entity(1);
        Entity e = new Entity(1);
        e.createComponent(LocationComponent.class);
        e.createComponent(SpeedComponent.class);
        e.createComponent(AccelerationComponent.class);

        world.createEntity(e);

        e.getComponent(AccelerationComponent.class).y += 10;


        StObject objectE = new StObject();
        e.save(objectE);

        System.out.println(objectE);
        StObject objectD = new StObject();
        d.load(objectE);
        d.save(objectD);
        System.out.println(objectD);
    }
}