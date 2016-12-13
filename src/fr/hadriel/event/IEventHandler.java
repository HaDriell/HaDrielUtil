package fr.hadriel.event;

/**
 * Created by glathuiliere setOn 08/08/2016.
 */
public interface IEventHandler<T extends IEvent> {
    public boolean handle(T event);
}
