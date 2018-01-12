package fr.hadriel.event;


/**
 * Created by glathuiliere on 08/08/2016.
 */
public interface IEvent {

    public static <T extends IEvent> IEvent dispatch(IEvent event, Class<T> eventType, IEventHandler<T> handler) {
        return eventType.isInstance(event) ? handler.handle(eventType.cast(event)) : event;
    }
}