package fr.hadriel.main.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 11/04/2017.
 */
public class MultiEventListener implements IEventListener {

    private Map<Class, IEventHandler> map;

    public MultiEventListener() {
        this.map = new HashMap<>();
    }

    public synchronized <T extends IEvent> void setEventHandler(Class<T> type, IEventHandler<T> handler) {
        //Arg validation
        if(type == null)
            throw new NullPointerException();

        //Effective setup
        if(handler == null)
            map.remove(type);
        else
            map.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    public synchronized IEvent onEvent(IEvent event) {
        if(event != null) {
            IEventHandler handler = map.get(event.getClass());
            if (handler != null)
                return handler.handle(event);
        }
        return event;
    }
}