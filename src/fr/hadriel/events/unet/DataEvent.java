package fr.hadriel.events.unet;

import fr.hadriel.event.IEvent;
import fr.hadriel.unet.Connection;

/**
 * Created by glathuiliere on 22/08/2016.
 */
public class DataEvent implements IEvent {

    public Connection connection;
    public byte[] data;
    public int channel;

    public DataEvent(Connection connection, byte[] data, int channel) {
        this.connection = connection;
        this.data = data;
        this.channel = channel;
    }
}