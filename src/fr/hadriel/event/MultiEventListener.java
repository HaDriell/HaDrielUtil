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

    /**
     * This method finds the handler for the fired event or by default tries to get the default handler <br/>
     * The default handler is supposed to be the IEvent event type
     * @param event the fired event
     * @return true if event has been handled
     */
    @SuppressWarnings("unchecked")
    public boolean onEvent(IEvent event) {
        IEventHandler handler = map.get(event.getClass()); // get the typed handler
        return handler != null && handler.handle(event);
    }
}
