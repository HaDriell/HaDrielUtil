package fr.hadriel.unet;

import fr.hadriel.events.input.UpdateEvent;
import fr.hadriel.events.unet.DataEvent;

import java.util.Arrays;

/**
 * Created by glathuiliere on 25/08/2016.
 */
public class KeepAliveManager {

    private static final byte[] KEEP_ALIVE_REQUEST = "KEEP_ALIVE".getBytes();
    private static final byte[] KEEP_ALIVE_RESPONSE = "KEPT_ALIVE".getBytes();

    public static final short KEEP_ALIVE_CHANNEL = -1;

    private ConnectionManager manager;

    public KeepAliveManager(ConnectionManager manager) {
        this.manager = manager;
    }

    public boolean onData(DataEvent event) {
        boolean request = Arrays.equals(event.data, KEEP_ALIVE_REQUEST);
        boolean response = Arrays.equals(event.data, KEEP_ALIVE_RESPONSE);
        if(request) {
            event.connection.send(KEEP_ALIVE_RESPONSE, KEEP_ALIVE_CHANNEL);
        }
        event.connection.resetIdleTime();
        return request || response;
    }

    public boolean onUpdate(UpdateEvent event) {
        manager.forEachConnection(connection -> {
            if(connection.shouldSendKeepAlive()) {
                connection.send(KEEP_ALIVE_REQUEST, KEEP_ALIVE_CHANNEL);
            }
        });
        return false;
    }
}