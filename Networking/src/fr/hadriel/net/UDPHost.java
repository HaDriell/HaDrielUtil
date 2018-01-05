package fr.hadriel.net;

import fr.hadriel.net.events.DataEvent;
import fr.hadriel.util.Buffer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author glathuiliere
 */
public abstract class UDPHost {
    private static final int DEFAULT_BUFFER_SIZE = 1400;

    private DatagramSocket socket;
    private Thread thread;
    private final byte[] buffer;


    public UDPHost() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public UDPHost(int bufferSize) {
        this.buffer = new byte[bufferSize];
    }

    private void run() {
        DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
        while(isBound()) {
            try {
                //Clear Packet
                packet.setAddress(null);
                packet.setPort(-1);
                packet.setData(buffer, 0, buffer.length);
                socket.receive(packet);

                if(packet.getAddress() != null && packet.getPort() > 0) {
//                    dispatcher.onEvent(new DataEvent(packet));
                }
            } catch (Throwable e) {
                throw new NetworkException("Error while receiving data", e);
            }
        }
    }

    public boolean isBound() {
        return socket != null && socket.isBound();
    }

    public void bind() {
        bind(0); // binds to an ephemeral port (random ~50000+)
    }

    public void bind(int port) {
        unbind();
        try {
            //setup socket
            socket = new DatagramSocket(port);
            socket.setSoTimeout(0);
            //setup daemon
            thread = new Thread(this::run);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException ignore) {
            unbind();
        }
    }

    public void unbind() {
        if(isBound()) {
            //clean-up daemon
            thread.interrupt();
            thread = null;
            //clean-up socket
            socket.close();
            socket = null;
        }
    }
}