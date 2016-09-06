package fr.hadriel.network.unet;

import fr.hadriel.network.UDPSocket;

import java.net.InetAddress;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class Connection {

    public final InetAddress address;
    public final int port;
    private int timeout;
    private int idleTime;

    private UDPSocket socket;

    public Connection(InetAddress address, int port, UDPSocket socket, int timeout) {
        this.address = address;
        this.port = port;
        this.socket = socket;
        this.timeout = timeout;
        this.idleTime = timeout / 2; // force instant KeepAliveChannel stuff !
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void send(byte[] data, int channel) {
        send(data, 0, data.length, channel);
    }

    public void send(byte[] data, int offset, int length, int channel) {
        if(!isClosed())
            socket.send(address, port, data, offset, length, channel);
    }

    public boolean isTargeting(InetAddress address, int port) {
        return this.address.equals(address) && this.port == port;
    }

    public void resetIdleTime() {
        idleTime = 0;
    }

    public void update(int elapsedTime) {
        idleTime += elapsedTime;
    }

    public boolean shouldSendKeepAlive() {
        return timeout / 2 < idleTime;
    }

    public boolean isConnected() {
        return !isClosed() && !isTimeout();
    }

    public boolean isTimeout() {
        return timeout < idleTime;
    }

    public boolean isClosed() {
        return socket == null;
    }

    void close() {
        socket = null;
    }

    public String toString() {
        return String.format("Connection(%s:%d)", address, port);
    }

    public int getIdleTime() {
        return idleTime;
    }
}