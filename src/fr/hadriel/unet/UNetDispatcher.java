package fr.hadriel.unet;


import fr.hadriel.event.EventHandlerChain;
import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventHandler;
import fr.hadriel.event.IEventListener;
import fr.hadriel.events.input.UpdateEvent;
import fr.hadriel.events.unet.ConnectionEvent;
import fr.hadriel.events.unet.DataEvent;
import fr.hadriel.events.unet.DisconnectionEvent;
import fr.hadriel.serialization.Serial;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 25/08/2016.
 */
public class UNetDispatcher implements IEventListener {

    private EventHandlerChain<ConnectionEvent> connectionHandlers;
    private EventHandlerChain<DisconnectionEvent> disconnectionHandlers;
    private Map<Integer, EventHandlerChain<DataEvent>> map;
    private EventHandlerChain<UpdateEvent> updateHandlers;

    public UNetDispatcher() {
        this.map = new HashMap<>();
        this.connectionHandlers = new EventHandlerChain<>();
        this.disconnectionHandlers = new EventHandlerChain<>();
        this.updateHandlers = new EventHandlerChain<>();
    }

    public void addDataHandler(IEventHandler<DataEvent> handler, int channel) {
        EventHandlerChain<DataEvent> chain = map.get(channel);
        if(chain == null) {
            chain = new EventHandlerChain<>();
            map.put(channel, chain);
        }
        chain.add(handler);
    }

    public void removeDataHandler(IEventHandler<DataEvent> handler, int channel) {
        EventHandlerChain<DataEvent> chain = map.get(channel);
        if(chain != null) {
            chain.remove(handler);
        }
    }

    public void addConnectionHandler(IEventHandler<ConnectionEvent> handler) {
        connectionHandlers.add(handler);
    }

    public void removeConnectionHandler(IEventHandler<ConnectionEvent> handler) {
        connectionHandlers.remove(handler);
    }

    public void addDisconnectionHandler(IEventHandler<DisconnectionEvent> handler) {
        disconnectionHandlers.add(handler);
    }

    public void removeDisconnectionHandler(IEventHandler<DisconnectionEvent> handler) {
        disconnectionHandlers.remove(handler);
    }

    public void addUpdateHandler(IEventHandler<UpdateEvent> handler) {
        updateHandlers.add(handler);
    }

    public void removeUpdateHandler(IEventHandler<UpdateEvent> handler) {
        updateHandlers.remove(handler);
    }

    public boolean dispatchEvent(DataEvent event, short channel) {
        EventHandlerChain<DataEvent> chain = map.get(channel);
        return chain != null && chain.handle(event);
    }

    public boolean onEvent(IEvent event) {
        if(event instanceof UpdateEvent) return updateHandlers.handle((UpdateEvent) event);
        else if(event instanceof ConnectionEvent) return connectionHandlers.handle((ConnectionEvent) event);
        else if(event instanceof DisconnectionEvent) return disconnectionHandlers.handle((DisconnectionEvent) event);
        else if(event instanceof DataEvent) {
            DataEvent dataEvent = (DataEvent) event;
            EventHandlerChain<DataEvent> chain = map.get(dataEvent.channel);
            return chain != null && chain.handle(dataEvent);
        }
        //Unsupported Event Type, return false
        return false;
    }
}