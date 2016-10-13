package fr.hadriel.network.unet;

import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.events.DataEvent;
import fr.hadriel.events.UpdateEvent;
import fr.hadriel.events.unet.ConnectionEvent;
import fr.hadriel.events.unet.DisconnectionEvent;

/**
 * Created by glathuiliere on 29/08/2016.
 */
public class UNetDispatcher implements IEventListener {

    private ConnectionManager connectionManager;
    private ChannelManager channelManager;

    public UNetDispatcher(UDPSocket socket) {
        this.channelManager = new ChannelManager();
        this.connectionManager = new ConnectionManager(socket);
    }

    public boolean onEvent(IEvent event) {
        if (event instanceof UpdateEvent) {
            connectionManager.onUpdate((UpdateEvent) event); // Internal Connection Management
            return channelManager.fireUpdateEvent((UpdateEvent) event);
        } else if (event instanceof DataEvent) {
            connectionManager.onData((DataEvent) event); // Internal Connection Management
            return channelManager.fireDataEvent((DataEvent) event);
        } else if (event instanceof ConnectionEvent) {
            return channelManager.fireConnectionEvent((ConnectionEvent) event);
        } else if (event instanceof DisconnectionEvent) {
            return channelManager.fireDisconnectionEvent((DisconnectionEvent) event);
        }
        return false;
    }
}