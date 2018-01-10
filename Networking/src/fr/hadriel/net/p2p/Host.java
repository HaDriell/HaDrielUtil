package fr.hadriel.net.p2p;

import fr.hadriel.event.DelegateEventListener;
import fr.hadriel.event.EventDispatcher;
import fr.hadriel.net.p2p.events.Connection;
import fr.hadriel.net.p2p.events.Disconnection;
import fr.hadriel.net.p2p.events.Packet;
import fr.hadriel.util.Buffer;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author glathuiliere
 */
public class Host {
    private static final int DEFAULT_MTU = 1400;
    private static final int DEFAULT_TIMEOUT = 5000;

    //Peers
    private List<Peer> peers;
    private Lock lock;

    //Watchers
    private Thread peerWatcher;
    private Thread socketWatcher;

    //Net Config
    private DatagramSocket socket;
    private int mtu;
    private int timeout;

    //Server Dispatcher
    private final EventDispatcher dispatcher;

    public Host() {
        this(DEFAULT_MTU, DEFAULT_TIMEOUT);
    }

    public Host(int timeout) {
        this(DEFAULT_MTU, timeout);
    }

    public Host(int mtu, int timeout) {
        this.dispatcher = new EventDispatcher();
        this.peers = new ArrayList<>();
        this.lock = new ReentrantLock();
        this.timeout = timeout;
        this.socket = null;
        this.mtu = mtu;

        //peer dispatcher data forward
        dispatcher.addEventListener(new DelegateEventListener(Packet.class, (event) -> event.peer.getDispatcher().onEvent(event)));
    }

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    public void bind() {
        bind(0);
    }

    public void bind(int port) {
        bind(null, port);
    }

    public synchronized void bind(InetAddress address, int port) {
        if(isBound())
            unbind();

        try {
            socket = new DatagramSocket(port, address);
            socket.setSoTimeout(0); // blocking mode ON

            //peer watcher setup
            peerWatcher = new Thread(this::watchPeers, "Peer Watcher");
            peerWatcher.setDaemon(true);
            peerWatcher.start();

            //socket watcher setup
            socketWatcher = new Thread(this::watchSocket, "Socket Watcher");
            socketWatcher.setDaemon(true);
            socketWatcher.start();
        } catch (Exception e) {
            throw new NetworkException("Unable to bind Host ", e);
        }
    }

    public synchronized void unbind() {
        if(!isBound())
            return;

        //stop socket watcher
        socketWatcher.interrupt();
        socketWatcher = null;

        //stop peer watcher
        peerWatcher.interrupt();
        peerWatcher = null;

        //gracefully close the socket
        try {
            socket.close();
        } catch (Exception ignore) {}
    }

    private Peer find(InetAddress address, int port) {
        Peer peer = null;
        lock.lock();
        for(Peer p : peers) {
            if(p.isTargeting(address, port)) {
                peer = p;
                break;
            }
        }
        lock.unlock();
        return peer;
    }

    public Peer connect(String hostname, int port) {
        try {
            return connect(InetAddress.getByName(hostname), port);
        } catch (UnknownHostException e) {
            throw new NetworkException("Unable to find Host ", e);
        }
    }

    public Peer connect(InetAddress address, int port) {
        Peer peer = find(address, port);
        if(peer == null) {
            peer = new Peer(this, address, port, timeout / 1000f);
            //Add effectively
            lock.lock();
            peers.add(peer);
            lock.unlock();
            //notify with a connection event
            dispatcher.onEvent(new Connection(peer));
        }
        return peer;
    }

    public int getMTU() {
        return mtu;
    }

    private void watchSocket() {
        while (isBound()) {
            DatagramPacket packet = new DatagramPacket(new byte[mtu], mtu);
            try {
                socket.receive(packet); // blocks the watcher until a packet is received
                Peer peer = connect(packet.getAddress(), packet.getPort()); // if connection is created it will send a PeerConnection event
                dispatcher.onEvent(new Packet(peer, packet)); //
            } catch (IOException e) {
                throw new NetworkException("Issue while Listening to the Socket", e);
            }
        }
    }

    private void watchPeers() {
        while(isBound()) {
            try {
                lock.lock();
                peers.stream().filter(Peer::isTimeout).forEach(p -> dispatcher.onEvent(new Disconnection(p))); // notify timeout with disconnection
                peers.removeIf(Peer::isTimeout); // actually remove peers
            } finally {
                lock.unlock();
            }

            // Avoid CPU burning (15 turns/sec is really decent)
            try { Thread.sleep(66); } catch (InterruptedException ignore) { }
        }
    }

    public synchronized InetAddress address() {
        return socket == null ? null : socket.getLocalAddress();
    }

    public synchronized int port() {
        return socket == null ? -1 : socket.getLocalPort();
    }

    public synchronized boolean isBound() {
        return socket != null && socket.isBound();
    }

    void send(Peer peer, Buffer buffer) {
        if(buffer.remaining() > mtu)
            throw new NetworkException("Buffer size too large (MTU = " + mtu + ")");

        try {
            DatagramPacket packet = new DatagramPacket(
                    buffer.array(),
                    buffer.position(),
                    buffer.limit(),
                    peer.getAddress(),
                    peer.getPort()
            );
            socket.send(packet);
        } catch (IOException e) {
            throw new NetworkException("Unable to send data ", e);
        }
    }
}