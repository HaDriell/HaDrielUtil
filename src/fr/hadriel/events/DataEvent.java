package fr.hadriel.events;

import fr.hadriel.event.IEvent;

import java.net.InetAddress;

/**
 * Created by glathuiliere on 22/08/2016.
 */
public class DataEvent implements IEvent {

    public InetAddress address;
    public int port;
    public byte[] data;
    public int channel;

    public DataEvent(InetAddress address, int port, byte[] data, int channel) {
        this.address = address;
        this.port = port;
        this.data = data;
        this.channel = channel;
    }
}