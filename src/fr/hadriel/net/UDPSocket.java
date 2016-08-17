package fr.hadriel.net;

import fr.hadriel.event.MultiEventListener;
import fr.hadriel.events.ConnectEvent;
import fr.hadriel.events.DataEvent;
import fr.hadriel.threading.Loop;

import java.io.IOException;
import java.net.*;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class UDPSocket extends Loop {

    public static final int DEFAULT_BUFFER_SIZE = 1400;

    public final MultiEventListener listener;
    public final InetAddress address;
    public final int port;

    private DatagramSocket socket;
    private UDPConnectionManager manager;

    //Receive buffer system
    private DatagramPacket receivePacket;

    public UDPSocket(InetAddress address, int port, int bufferSize) {
        this.listener = new MultiEventListener();
        this.manager = new UDPConnectionManager(this);
        this.address = address;
        this.port = port;
        setBufferSize(bufferSize);
    }

    public UDPSocket(InetAddress address, int port) {
        this(address, port, DEFAULT_BUFFER_SIZE);
    }

    public UDPSocket(int port) {
        this(null, port);
    }

    public UDPSocket() {
        this(0);
    }

    public void setBufferSize(int bufferSize) {
        this.receivePacket = new DatagramPacket(new byte[bufferSize], bufferSize);
    }

    public int getBufferSize() {
        return receivePacket.getData().length;
    }

    protected void onStart() {
        try {
            socket = new DatagramSocket(port, address);
            socket.setSoTimeout(1000); // thread will block for 1 second max
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    protected void onLoop() {
        //In case onStart failed
        if(socket == null) {
            interrupt();
            return;
        }

        //Update connections
        manager.updateConnections();
        //Listen over the network
        try {
            socket.receive(receivePacket);
        } catch (IOException ignore) {
            return;
        }

        //Process Data
        InetAddress address = receivePacket.getAddress();
        int port = receivePacket.getPort();
        UDPConnection connection = manager.getConnection(address, port);

        //Connection detected
        if(connection == null) {
            connection = manager.createConnection(address, port);
            listener.onEvent(new ConnectEvent(connection));
        }
        connection.refresh();
        listener.onEvent(new DataEvent(connection, receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength()));
    }

    protected void onStop() {
        socket.close();
        socket = null;
    }

    public void send(byte[] data, InetAddress address, int port) {
        send(data, 0, data.length, address, port);
    }

    public void send(byte[] data, int offset, int length, InetAddress address, int port) {
        try {
            socket.send(new DatagramPacket(data, offset, length, address, port));
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }
}