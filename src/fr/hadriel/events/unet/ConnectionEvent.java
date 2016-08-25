package fr.hadriel.events.unet;

import fr.hadriel.event.IEvent;
import fr.hadriel.unet.Connection;

/**
 * Created by glathuiliere on 22/08/2016.
 */
public class ConnectionEvent implements IEvent {

    public Connection connection;

    public ConnectionEvent(Connection connection) {
        this.connection = connection;
    }
}