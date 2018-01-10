package fr.hadriel.net.p2p;

import fr.hadriel.net.p2p.events.PeerObjectOutput;
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

    private final Timer timeoutWatcher;
    private final int timeout;
    private boolean closed;

    public Peer(Host host, InetAddress address, int port, int timeout) {
        this.address = Objects.requireNonNull(address);
        this.host = Objects.requireNonNull(host);
        this.timeoutWatcher = new Timer();
        this.timeout = timeout;
        this.closed = false;
        this.port = port;
    }

    public InetAddress address() {
        return address;
    }

    public int port() {
        return port;
    }

    /**
     * @return true if the Peer timed out
     */
    public boolean isTimeout() {
        return closed || timeoutWatcher.elapsed() > timeout;
    }

    /**
     * @return true if 25% of the timeout limit has been reached
     */
    public boolean isIdle() {
        return !closed && timeoutWatcher.elapsed() > timeout / 4;
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

    /**
     * Resets the timeoutWatcher Timer. mainly used to locally keep connected a Peer
     */
    public void refresh() {
        timeoutWatcher.reset();
    }

    public void send(Object object, boolean reliable) {
        host.getDispatcher().onEvent(new PeerObjectOutput(this, object, reliable));
    }

    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof Peer) {
            Peer p = (Peer) obj;
            return host == p.host && port == p.port && address.equals(p.address);
        }
        return super.equals(obj);
    }
}