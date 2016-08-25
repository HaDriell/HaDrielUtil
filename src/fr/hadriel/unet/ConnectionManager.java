package fr.hadriel.unet;

import fr.hadriel.events.unet.*;
import fr.hadriel.time.Timer;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by glathuiliere on 22/08/2016.
 */
public class ConnectionManager {

    private List<Connection> connections;
    private UNetSocket socket;
    private Timer timer;

    public ConnectionManager(UNetSocket socket) {
        this.socket = socket;
        this.connections = new ArrayList<>();
        this.timer = new Timer();
    }

    public synchronized Connection get(InetAddress address, int port) {
        for(Connection connection : connections) {
            if(connection.isTargeting(address, port))
                return connection;
        }
        return null;
    }

    public synchronized Connection create(InetAddress address, int port, int timeout) {
        Connection connection = get(address, port);
        if(connection != null) { //pick secretly an old existing connection
            connection.setTimeout(timeout);
        } else {
            connection = new Connection(address, port, socket, timeout);
            connections.add(connection);
        }
        return connection;
    }

    public synchronized boolean destroy(InetAddress address, int port) {
        Connection connection = get(address, port);
        return connections.remove(connection);
    }

    public void forEachConnection(Consumer<Connection> consumer) {
        connections.forEach(consumer);
    }

    private boolean onDataEvent(DataEvent event) {
        event.connection.resetIdleTime();
        return false;
    }
}