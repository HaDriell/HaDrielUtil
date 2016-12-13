package fr.hadriel.ecs.processors;

import fr.hadriel.ecs.Entity;
import fr.hadriel.ecs.EntityProcessor;
import fr.hadriel.ecs.components.AccelerationComponent;
import fr.hadriel.ecs.components.LocationComponent;
import fr.hadriel.ecs.components.SpeedComponent;
import fr.hadriel.math.Vec2;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public class MovementProcessor extends EntityProcessor {

    private Vec2 gravity;
    private float friction;

    public MovementProcessor(Vec2 gravity, float friction) {
        this.gravity = gravity;
        this.friction = friction;
    }

    protected Class[] requires() {
        return new Class[] {
                LocationComponent.class,
                SpeedComponent.class,
                AccelerationComponent.class
        };
    }

    public void update(Entity entity, float delta) {
        LocationComponent location = entity.getComponent(LocationComponent.class);
        SpeedComponent speed = entity.getComponent(SpeedComponent.class);
        AccelerationComponent acceleration = entity.getComponent(AccelerationComponent.class);
        location.x += speed.x;
        location.y += speed.y;
        location.angle += speed.rotation;

        speed.x += acceleration.x;
        speed.y += acceleration.y;
        speed.rotation += acceleration.torque;
    }
}