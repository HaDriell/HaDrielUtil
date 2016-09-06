package fr.hadriel.events.unet;

import fr.hadriel.event.IEvent;

import java.net.InetAddress;

/**
 * Created by glathuiliere on 22/08/2016.
 */
public class DisconnectionEvent implements IEvent {


    public InetAddress address;
    public int port;

    public DisconnectionEvent(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }
}