package fr.hadriel.main.event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public interface IEventHandler<T extends IEvent> {

    /**
     * This Class is responsible of the handling of a concrete IEvent.
     * @param event the event to handle.
     * @return the event that will be dispatched after this handle. null if capture should stop.
     */
    public IEvent handle(T event);
}