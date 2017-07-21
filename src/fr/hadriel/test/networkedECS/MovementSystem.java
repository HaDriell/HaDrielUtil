package fr.hadriel.test.networkedECS;

import fr.hadriel.main.ecs.EntityDataManager;
import fr.hadriel.main.ecs.EntitySystem;
import fr.hadriel.main.ecs.filter.Filters;
import fr.hadriel.main.math.Vec2;

/**
 * Created by glathuiliere on 10/04/2017.
 */
public class MovementSystem extends EntitySystem {

    public MovementSystem() {
        super(Filters.Require("position", Vec2.class),
                Filters.Require("speed", Vec2.class));
    }

    @Override
    protected void begin() {}

    @Override
    protected void update(EntityDataManager manager, long id, float delta) {
        //Get components
        Vec2 position = manager.getComponent(id, "position", Vec2.class);
        Vec2 speed = manager.getComponent(id, "speed", Vec2.class);

        //Actual gameplay rule
        position = position.add(speed.scale(delta, delta));

        //Set components (optional here, they're not )
        manager.setComponent(id, "position", position);
        manager.setComponent(id, "speed", speed);
    }

    @Override
    protected void end() {}
}