package fr.hadriel.net;

import fr.hadriel.threading.Loop;

import java.io.IOException;
import java.net.*;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public abstract class UDPSocket extends Loop {

    public final InetAddress address;
    public final int port;
    private DatagramSocket socket;

    //Receive buffer system
    private DatagramPacket receivePacket;

    public UDPSocket(InetAddress address, int port, int bufferSize) {
        this.address = address;
        this.port = port;
        this.receivePacket = new DatagramPacket(new byte[bufferSize], bufferSize);
    }

    public UDPSocket(int port, int bufferSize) {
        this(null, port, bufferSize);
    }

    public UDPSocket(int bufferSize) {
        this(0, bufferSize);
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

        //Listen over the network
        try {
            socket.receive(receivePacket);
        } catch (IOException ignore) {
            return; // no data received
        }

        //copy data (useful ?)
        byte[] buffer = new byte[receivePacket.getLength()];
        System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), buffer, 0, receivePacket.getLength());
        onReceive(buffer); //fire Event
    }

    protected void onStop() {
        socket.close();
        socket = null;
    }

    public void send(byte[] data, InetAddress address, int port) {
        try {
            socket.send(new DatagramPacket(data, 0, data.length, address, port));
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }

    public abstract void onReceive(byte[] data);
}