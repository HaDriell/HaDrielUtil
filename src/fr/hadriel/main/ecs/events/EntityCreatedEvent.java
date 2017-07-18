package fr.hadriel.main.ecs.events;


/**
 * Created by glathuiliere on 30/03/2017.
 */
public class EntityCreatedEvent implements ECSEvent {
    public final long id;

    public EntityCreatedEvent(long id) {
        this.id = id;
    }
}
