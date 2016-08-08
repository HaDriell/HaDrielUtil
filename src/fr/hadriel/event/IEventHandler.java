package fr.hadriel.event;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public interface IEventHandler<T extends IEvent> {
    public boolean handle(T event);
}
