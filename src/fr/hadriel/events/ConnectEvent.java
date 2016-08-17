package fr.hadriel.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.net.UDPConnection;

import java.net.InetAddress;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class ConnectEvent implements IEvent {

    public UDPConnection connection;

    public ConnectEvent(UDPConnection connection) {
        this.connection = connection;
    }
}