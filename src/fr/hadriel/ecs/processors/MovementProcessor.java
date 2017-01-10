package fr.hadriel.ecs.processors;

import fr.hadriel.ecs.Entity;
import fr.hadriel.ecs.EntityProcessor;
import fr.hadriel.ecs.components.AccelerationComponent;
import fr.hadriel.ecs.components.LocationComponent;
import fr.hadriel.ecs.components.SpeedComponent;

/**
 * Created by HaDriel setOn 09/12/2016.
 */
public class MovementProcessor extends EntityProcessor {


    public MovementProcessor() {
        super(new Class[] {LocationComponent.class, SpeedComponent.class, AccelerationComponent.class});
    }

    public void update(Entity entity, float delta) {
        LocationComponent location = entity.getComponent(LocationComponent.class);
        SpeedComponent speed = entity.getComponent(SpeedComponent.class);
        AccelerationComponent acceleration = entity.getComponent(AccelerationComponent.class);

        if(acceleration.x != 0 || acceleration.y != 0 || acceleration.torque != 0) {
            speed.x += acceleration.x * delta;
            speed.y += acceleration.y * delta;
            speed.rotation += acceleration.torque;
            speed.modified = true;
        }

        if(speed.x != 0 || speed.y != 0) {
            location.x += speed.x * delta;
            location.y += speed.y * delta;
            location.angle += speed.rotation;
            location.modified = true;
        }
    }

    public void beforeUpdate(float delta) {}

    public void afterUpdate(float delta) {}
}