package fr.hadriel.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.net.UDPConnection;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class DisconnectEvent implements IEvent {

    public UDPConnection connection;

    public DisconnectEvent(UDPConnection connection) {
        this.connection = connection;
    }
}