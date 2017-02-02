package fr.hadriel.event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public interface IEventHandler<T extends IEvent> {

    /**
     * Returns true if the T event should be stopped
     * @param event
     * @return
     */
    public boolean handle(T event);
}
