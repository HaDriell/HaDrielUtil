package fr.hadriel.network.unet;

import fr.hadriel.event.IEventHandler;
import fr.hadriel.events.UpdateEvent;
import fr.hadriel.events.unet.ConnectionEvent;
import fr.hadriel.events.DataEvent;
import fr.hadriel.events.unet.DisconnectionEvent;

/**
 * Created by glathuiliere on 29/08/2016.
 */
public abstract class UNetChannel {

    public final IEventHandler<DataEvent> dataHandler;
    public final IEventHandler<ConnectionEvent> connectionHandler;
    public final IEventHandler<DisconnectionEvent> disconnectionHandler;
    public final IEventHandler<UpdateEvent> updateHandler;

    protected UNetChannel() {
        this.dataHandler = this::onData;
        this.connectionHandler = this::onConnection;
        this.disconnectionHandler = this::onDisconnection;
        this.updateHandler = this::onUpdate;
    }

    public abstract boolean onData(DataEvent event);

    public abstract boolean onConnection(ConnectionEvent event);

    public abstract boolean onDisconnection(DisconnectionEvent event);

    public abstract boolean onUpdate(UpdateEvent event);
}