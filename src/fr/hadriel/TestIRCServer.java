package fr.hadriel;

import fr.hadriel.events.ConnectEvent;
import fr.hadriel.events.DataEvent;
import fr.hadriel.events.DisconnectEvent;
import fr.hadriel.net.UDPConnection;
import fr.hadriel.net.UDPSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 17/08/2016.
 */
public class TestIRCServer {
    public static void main(String[] args) {
        UDPSocket server = new UDPSocket(7777);
        List<UDPConnection> connections = new ArrayList<>();

        server.listener.addHandler(ConnectEvent.class, (event -> {
            System.out.println("[SERVER] " + event.connection + " connected");
            connections.add(event.connection);
            return true;
        }));

        server.listener.addHandler(DisconnectEvent.class, (event -> {
            System.out.println("[SERVER] " + event.connection + " disconnected");
            connections.remove(event.connection);
            return true;
        }));

        server.listener.addHandler(DataEvent.class, (event -> {
            System.out.println("[SERVER] " + event.connection + ":" + new String(event.data, event.dataOffset, event.dataLength));
            for(UDPConnection connection : connections) {
                connection.send(event.data, event.dataOffset, event.dataLength);
            }
            return true;
        }));
        server.start();
    }
}
