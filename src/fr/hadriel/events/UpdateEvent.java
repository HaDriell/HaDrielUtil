package fr.hadriel.events;

import fr.hadriel.event.IEvent;

/**
 * Created by glathuiliere on 22/08/2016.
 *
 * This event is used to simulate loops in event systems, by getting fired frequently
 */
public class UpdateEvent implements IEvent {

    public float delta;

    public UpdateEvent(float delta) {
        this.delta = delta;
    }
}