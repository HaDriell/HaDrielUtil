package fr.hadriel.net;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPSocket {
    private static final int DEFAULT_SOCKET_PORT = 0;

    private DatagramChannel channel;

    public UDPSocket() {
        this.channel = null;
    }

    public SocketAddress local() {
        checkBinding();
        try {
            return channel.getLocalAddress();
        } catch (IOException ignore) {
            return null;
        }
    }

    private void checkBinding() {
        if(!isBound())
            throw new RuntimeException("UDPSocket NOT bound !");
    }

    public boolean isBound() {
        return channel != null && channel.isOpen();
    }

    public void bind() {
        bind(DEFAULT_SOCKET_PORT);
    }

    public synchronized void bind(int port) {
        if(isBound())
            throw new RuntimeException("Channel already bound");
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            throw new RuntimeException("Unable to bind Channel : ", e);
        }
    }

    public SocketAddress receive(ByteBuffer buffer) {
        return receive(buffer, 0); // blocking-mode
    }

    public SocketAddress receive(ByteBuffer buffer, int timeout) {
        try {
            channel.socket().setSoTimeout(timeout);
            return channel.receive(buffer);
        } catch (SocketTimeoutException ignore) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Unable to receive data from channel", e);
        }
    }

    public void send(ByteBuffer buffer, SocketAddress target) {
        checkBinding();
        try {
            channel.send(buffer, target);
        } catch (IOException e) {
            throw new RuntimeException("Unable to send data to channel", e);
        }
    }

    public synchronized void unbind() {
        if(channel == null) return;
        try {
            channel.close();
        } catch (IOException ignore) {}
        channel = null;
    }
}
