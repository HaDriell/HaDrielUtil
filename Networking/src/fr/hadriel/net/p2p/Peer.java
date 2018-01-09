package fr.hadriel.net.p2p;

import fr.hadriel.event.EventDispatcher;
import fr.hadriel.util.Buffer;
import fr.hadriel.util.Timer;

import java.net.InetAddress;
import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public final class Peer {

    private final InetAddress address;
    private final int port;
    private final Host host;

    private final Timer watcher;
    private final float timeout;
    private boolean closed;

    private final EventDispatcher dispatcher;

    public Peer(Host host, InetAddress address, int port, float timeout) {
        this.address = Objects.requireNonNull(address);
        this.host = Objects.requireNonNull(host);
        this.dispatcher = new EventDispatcher();
        this.watcher = new Timer();
        this.timeout = timeout;
        this.closed = false;
        this.port = port;
    }

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    /**
     * @return true if the Peer timed out
     */
    public boolean isTimeout() {
        return closed || watcher.elapsed() > timeout;
    }

    /**
     *
     * @param address the target address
     * @param port the target port
     * @return true if the Peer targets to this Socket address+port
     */
    public boolean isTargeting(InetAddress address, int port) {
        return !isTimeout() && this.port == port && this.address.equals(address);
    }

    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof Peer) {
            Peer p = (Peer) obj;
            return host == p.host && port == p.port && address.equals(p.address);
        }
        return super.equals(obj);
    }

    /**
     * Resets the watcher Timer. mainly used to locally keep connected a Peer
     */
    public void refresh() {
        watcher.reset();
    }


    public void send(Buffer buffer) {
        host.send(this, buffer);
    }
}