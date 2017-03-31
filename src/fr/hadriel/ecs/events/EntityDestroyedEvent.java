package fr.hadriel.ecs.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public class EntityDestroyedEvent implements IEvent {
    public final long id;

    public EntityDestroyedEvent(long id) {
        this.id = id;
    }
}
