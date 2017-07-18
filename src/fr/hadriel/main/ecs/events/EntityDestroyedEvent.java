package fr.hadriel.main.ecs.events;


/**
 * Created by glathuiliere on 30/03/2017.
 */
public class EntityDestroyedEvent implements ECSEvent {
    public final long id;

    public EntityDestroyedEvent(long id) {
        this.id = id;
    }
}
