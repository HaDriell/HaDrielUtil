package fr.hadriel.net;

import fr.hadriel.time.Timer;

import java.net.InetAddress;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class UDPConnection {

    public final InetAddress address;
    public final int port;
    public int timeout;
    public boolean keepAlive;

    private UDPConnectionManager manager;
    private Timer timer;

    public UDPConnection(InetAddress address, int port, UDPConnectionManager manager, int timeoutMilliseconds) {
        this.address = address;
        this.port = port;
        this.manager = manager;
        this.timeout = timeoutMilliseconds;
        this.timer = new Timer();
        this.keepAlive = false;
    }

    public void send(byte[] data) {
        send(data, 0, data.length);
    }

    public void send(byte[] data, int offset, int length) {
        if(!isClosed())
            manager.send(data, offset, length, this);
    }

    void refresh() {
        timer.reset();
    }

    public boolean isConnected() {
        return !keepAlive && timer.elapsed() * 1000 <= timeout && !isClosed();
    }

    public boolean isTargeting(InetAddress address, int port) {
        return this.address.equals(address) && this.port == port;
    }

    public boolean isClosed() {
        return manager == null;
    }

    public void close() {
        manager = null;
    }

    public String toString() {
        return String.format("UDP Connection(%s:%d)", address, port);
    }
}