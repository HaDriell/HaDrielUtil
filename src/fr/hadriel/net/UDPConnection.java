package fr.hadriel.net;

import java.net.InetAddress;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class UDPConnection {

    public final InetAddress address;
    public final int port;
    private UDPSocket udpSocket;

    public UDPConnection(InetAddress address, int port, UDPSocket udpSocket) {
        this.address = address;
        this.port = port;
        this.udpSocket = udpSocket;
    }

    public void send(byte[] data) {
        udpSocket.send(data, address, port);
    }
}