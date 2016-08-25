package fr.hadriel;

import fr.hadriel.events.unet.ConnectionEvent;
import fr.hadriel.events.unet.DisconnectionEvent;
import fr.hadriel.unet.UNetSocket;
import fr.hadriel.unet.Connection;

import java.net.InetAddress;

/**
 * Created by glathuiliere on 17/08/2016.
 */
public class TestKeepAlive {
    public static void main(String[] args) throws Exception {
        UNetSocket a = new UNetSocket(InetAddress.getLocalHost(), 7000);
        UNetSocket b = new UNetSocket(InetAddress.getLocalHost(), 8000);
        a.getDispatcher().addConnectionHandler(TestKeepAlive::onConnection);
        b.getDispatcher().addConnectionHandler(TestKeepAlive::onConnection);
        a.getDispatcher().addDisconnectionHandler(TestKeepAlive::onDisonnection);
        b.getDispatcher().addDisconnectionHandler(TestKeepAlive::onDisonnection);
        b.getDispatcher().addDataHandler((event) -> {
            System.out.println("B RECV:" + new String(event.data));
            return true;
        }, (short) 1);
        a.start();
        b.start();
        Connection cb = a.connect(b.getAddress(), b.getPort());
        cb.send("\"data...\"".getBytes(), 1);
    }

    private static boolean onConnection(ConnectionEvent event) {
        System.out.println(event.connection + " connected");
        return false;
    }

    private static boolean onDisonnection(DisconnectionEvent event) {
        System.out.println(event.connection + " disconnected");
        return false;
    }
}