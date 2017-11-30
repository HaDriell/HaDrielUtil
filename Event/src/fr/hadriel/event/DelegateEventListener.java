package fr.hadriel.event;

/**
 * Created by glathuiliere on 10/04/2017.
 */
public class DelegateEventListener implements IEventListener {

    private Class<? extends IEvent> eventType;
    private IEventHandler eventHandler;

    public <T extends IEvent> DelegateEventListener(Class<T> type, IEventHandler<T> handler) {
        this.eventType = type;
        this.eventHandler = handler;
    }


    @SuppressWarnings("unchecked")
    public IEvent onEvent(IEvent event) {
        return eventHandler != null && eventType.isInstance(event) ? eventHandler.handle(eventType.cast(event)) : event;
    }
}