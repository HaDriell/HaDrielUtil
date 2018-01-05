package fr.hadriel.net.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.net.NetworkException;

import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author glathuiliere
 */
public class NetworkEvent implements IEvent {

    public final InetAddress address;
    public final int port;

    public NetworkEvent(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public NetworkEvent(String hostname, int port) {
        try {
            this.address = InetAddress.getByName(hostname);
            this.port = port;
        } catch (IOException e) {
            throw new NetworkException("Error while resolving " + hostname, e);
        }
    }
}