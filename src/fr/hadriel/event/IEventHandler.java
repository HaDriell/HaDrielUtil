package fr.hadriel.event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public interface IEventHandler<T extends IEvent> {

    /**
     * This function is called by IEventListeners when onEvent(IEvent event) is called
     * @param event the event instance
     * @return true if this event should stop it's propagation
     */
    public boolean handle(T event);
}
