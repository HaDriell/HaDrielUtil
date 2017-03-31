package fr.hadriel.event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public interface IEventHandler<T extends IEvent> {

    /**
     * This function is called by IEventListeners when onEvent(Event events) is called
     * @param event the events instance
     * @return true if this events should stop it's propagation
     */
    public void handle(T event);
}
