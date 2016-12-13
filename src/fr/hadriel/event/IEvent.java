package fr.hadriel.event;

/**
 * Created by glathuiliere setOn 08/08/2016.
 */
public interface IEvent {
    public static <T extends IEvent> boolean dispatch(IEvent event, Class<T> type, IEventHandler<T> handler) {
        return event.getClass() == type && handler.handle(type.cast(event));
    }
}
