package fr.hadriel.udp.p2p;

import fr.hadriel.util.Timer;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public final class UDPConnection {

    public static final byte CONNECTED = 1;
    public static final byte IDLE      = 2;
    public static final byte CLOSED    = 3;

    //Network Informations
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    //Connection management
    private final Timer keepAliveWatcher;
    private final Timer idleWatcher;
    private byte state;

    public UDPConnection(DatagramSocket socket, InetAddress address, int port) {
        this.address = Objects.requireNonNull(address);
        this.socket = Objects.requireNonNull(socket);
        this.idleWatcher = new Timer();
        this.keepAliveWatcher = new Timer();
        this.state = CONNECTED;
        this.port = port;
    }

    public InetAddress address() {
        return address;
    }

    public int port() {
        return port;
    }

    public boolean isConnected() {
        return state == CONNECTED || isIdle();
    }

    public boolean isIdle() {
        return state == IDLE;
    }

    public boolean isClosed() {
        return state == CLOSED;
    }


    public void update(int timeoutMilliseconds) {
        //TODO : lookup watchers.
        //TODO : handle automatic Keep Alives
    }


    /**
     * Closes a UDPConnection. A closed UDPConnection is not usable anymore
     */
    public void close() {
        if(state == CLOSED) return;
        //TODO : send graceful close packet ?
        state = CLOSED;
    }

    /**
     *
     * @param address the target address
     * @param port the target port
     * @return true if the UDPConnection targets to this Socket address+port
     */
    public boolean isTargeting(InetAddress address, int port) {
        return this.port == port && this.address.equals(address);
    }

    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof UDPConnection) {
            UDPConnection p = (UDPConnection) obj;
            return socket == p.socket && port == p.port && address.equals(p.address);
        }
        return super.equals(obj);
    }
}