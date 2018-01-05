package fr.hadriel.net;

import fr.hadriel.util.Buffer;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;

public class UDPSocket {
    public static final int RANDOM_SOCKET_PORT = 0;

    private DatagramChannel channel;

    public UDPSocket() {
        this.channel = null;
    }

    private void checkBinding() {
        if(!isBound())
            throw new RuntimeException("UDPSocket NOT bound !");
    }

    public boolean isBound() {
        return channel != null && channel.isOpen();
    }

    public void bind() {
        bind(RANDOM_SOCKET_PORT);
    }

    public synchronized void bind(int port) {
        if(isBound())
            throw new RuntimeException("Channel already bound");
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            throw new RuntimeException("Unable to bind channel", e);
        }
    }

    public SocketAddress receive(Buffer buffer) {
        return receive(buffer, 0); // blocking-mode
    }

    public SocketAddress receive(Buffer buffer, int timeout) {
        try {
            channel.socket().setSoTimeout(timeout);
            return channel.receive(buffer.asByteBuffer());
        } catch (SocketTimeoutException ignore) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Unable to receive data from channel", e);
        }
    }

    public void send(Buffer buffer, SocketAddress target) {
        checkBinding();
        try {
            channel.send(buffer.asByteBuffer(), target);
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
