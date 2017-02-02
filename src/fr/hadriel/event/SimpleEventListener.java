package fr.hadriel.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class SimpleEventListener implements IEventListener {

    private Class<? extends IEvent> type;
    private IEventHandler handler;

    public SimpleEventListener() {} // default constructor for noobs

    public <T extends IEvent> SimpleEventListener(Class<T> type, IEventHandler<T> handler) {
        setOn(type, handler);
    }

    public synchronized <T extends IEvent> void setOn(Class<T> type, IEventHandler<T> handler) {
        this.type = type;
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean onEvent(IEvent event) {
        return handler != null && event.getClass() == type && handler.handle(event);
    }
}