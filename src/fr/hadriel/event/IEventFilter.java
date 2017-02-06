package fr.hadriel.event;

/**
 * Created by gauti on 05/02/2017.
 */
public interface IEventFilter<T extends Event> {
    public boolean accept(T event);
}