package fr.hadriel.net;

import fr.hadriel.events.DisconnectEvent;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 16/08/2016.
 */
public class UDPConnectionManager {

    public static final int DEFAULT_TIMEOUT = 10000;

    private List<UDPConnection> connections;
    private UDPSocket localSocket;

    public UDPConnectionManager(UDPSocket localSocket) {
        this.connections = new ArrayList<>();
        this.localSocket = localSocket;
    }

    public synchronized UDPConnection getConnection(InetAddress address, int port) {
        for(UDPConnection connection : connections) {
            if(connection.isTargeting(address, port))
                return connection;
        }
        return null;
    }

    public synchronized UDPConnection createConnection(InetAddress address, int port) {
        UDPConnection connection = new UDPConnection(address, port, this, DEFAULT_TIMEOUT);
        connections.add(connection);
        return connection;
    }

    public synchronized void deleteConnection(UDPConnection connection) {
        connections.remove(connection);
        connection.close();
        localSocket.listener.onEvent(new DisconnectEvent(connection));
    }

    public synchronized void updateConnections() {
        int i = 0;
        while(i < connections.size()) {
            UDPConnection connection = connections.get(i);
            if(connection.isConnected()) {
                i++;
            } else {
                deleteConnection(connection);
            }
        }
    }

    public void send(byte[] data, UDPConnection connection) {
        send(data, 0, data.length, connection);
    }

    public void send(byte[] data, int offset, int length, UDPConnection connection) {
        localSocket.send(data, offset, length, connection.address, connection.port);
    }
}