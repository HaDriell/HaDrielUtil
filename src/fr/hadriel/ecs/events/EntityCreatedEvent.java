package fr.hadriel.ecs.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public class EntityCreatedEvent implements IEvent {
    public final long id;

    public EntityCreatedEvent(long id) {
        this.id = id;
    }
}
