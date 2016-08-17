package fr.hadriel.events;

import fr.hadriel.event.IEvent;
import fr.hadriel.net.UDPConnection;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class DataEvent implements IEvent {

    public byte[] data;
    public int dataOffset;
    public int dataLength;
    public UDPConnection connection;

    public DataEvent(UDPConnection connections, byte[] data, int dataOffset, int dataLength) {
        this.connection = connections;
        this.data = data;
        this.dataOffset = dataOffset;
        this.dataLength = dataLength;
    }
}