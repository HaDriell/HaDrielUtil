package fr.hadriel.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere setOn 13/12/2016.
 */
public class MultiEventListener implements IEventListener {

    private Map<Class<? extends IEvent>, IEventHandler> map;


    public MultiEventListener() {
        this.map = new HashMap<>();
    }

    public synchronized <T extends IEvent> void setOn(Class<T> type, IEventHandler<T> handler) {
        map.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    public boolean onEvent(IEvent event) {
        IEventHandler handler = map.get(event.getClass());
        return handler != null && handler.handle(event);
    }
}
