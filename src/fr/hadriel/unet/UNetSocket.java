package fr.hadriel.unet;

import fr.hadriel.events.unet.ConnectionEvent;
import fr.hadriel.events.unet.DataEvent;
import fr.hadriel.events.input.UpdateEvent;
import fr.hadriel.events.unet.DisconnectionEvent;
import fr.hadriel.serialization.Serial;
import fr.hadriel.threading.Loop;
import fr.hadriel.time.Timer;
import fr.hadriel.util.Property;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class UNetSocket extends Loop {

    public static final int DEFAULT_BUFFER_SIZE = 1400;

    private InetAddress address;
    private int port;
    private DatagramSocket socket;

    //Core System
    private int connectionTimeout = 5000; //TODO : add some API calls, this is disgusting
    private Timer timer;
    private ConnectionManager manager;
    private UNetDispatcher dispatcher;
    private KeepAliveManager keepAlive;

    //Receive system
    private Property<Integer> bufferSizeProperty;
    private DatagramPacket receivePacket;

    public UNetSocket(InetAddress address, int port, int bufferSize) {
        this.timer = new Timer();
        this.dispatcher = new UNetDispatcher();
        this.dispatcher.addUpdateHandler(this::onUpdateEvent);
        this.manager = new ConnectionManager(this);
        this.keepAlive = new KeepAliveManager(manager);
        this.dispatcher.addUpdateHandler(keepAlive::onUpdate);
        this.dispatcher.addDataHandler(keepAlive::onData, KeepAliveManager.KEEP_ALIVE_CHANNEL);
        this.address = address;
        this.port = port;

        this.bufferSizeProperty = new Property<>(bufferSize, DEFAULT_BUFFER_SIZE);
        this.bufferSizeProperty.addCallback((size) -> receivePacket = new DatagramPacket(new byte[bufferSize], bufferSize));
        this.receivePacket = new DatagramPacket(new byte[bufferSize], bufferSize); // initialization set
    }

    public UNetSocket(InetAddress address, int port) {
        this(address, port, DEFAULT_BUFFER_SIZE);
    }

    public UNetSocket(int port, int bufferSize) {
        this(null, port, bufferSize);
    }

    public UNetSocket(int port) {
        this(null, port, DEFAULT_BUFFER_SIZE);
    }

    public UNetSocket() {
        this(null, 0, DEFAULT_BUFFER_SIZE);
    }

    public int getBufferSize() {
        return bufferSizeProperty.get();
    }

    public void setBufferSize(int bufferSize) {
        bufferSizeProperty.set(bufferSize);
    }

    protected void onStart() {
        try {
            socket = new DatagramSocket(port, address);
            address = socket.getLocalAddress();
            port = socket.getLocalPort();
            socket.setSoTimeout(500); // thread will block for 1 second max
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

        //Clean receivePacket address
        receivePacket.setAddress(null);

        //Listen over the network
        try {
            socket.receive(receivePacket);
        } catch (IOException ignore) {}

        //Extract Data
        InetAddress address = receivePacket.getAddress();
        int port = receivePacket.getPort();
        byte[] buffer = receivePacket.getData();
        int offset = receivePacket.getOffset();
        int length = receivePacket.getLength();

        //Process Data
        // Update listener (useful for connection management)
        dispatcher.onEvent(new UpdateEvent());

        //No Data case
        if(address == null)
            return;

        // Data received
        Connection connection = connect(address, port); //connect works fine even if it's not a real connection lol
        connection.resetIdleTime();

        //Read Channel and update offset and length
        int channel = Serial.readInt(buffer, offset);
        offset += 4;
        byte[] data = new byte[length - offset];
        System.arraycopy(buffer, 4, data, 0, data.length);
        dispatcher.onEvent(new DataEvent(connection, data, channel));
    }

    protected void onStop() {
        socket.close();
        socket = null;
    }

    private boolean onUpdateEvent(UpdateEvent event) {
        List<Connection> list = new ArrayList<>();
        int elapsed = (int) (timer.elapsed() * 1000);
        timer.reset();
        manager.forEachConnection((connection -> {
            connection.update(elapsed);
            if (!connection.isConnected())
                list.add(connection);
        }));
        for(Connection connection : list)
            disconnect(connection);
        return false;
    }

    public Connection connect(InetAddress address, int port) {
        Connection connection = manager.get(address, port);
        if(connection == null) {
            connection = manager.create(address, port, connectionTimeout);
            dispatcher.onEvent(new ConnectionEvent(connection));
        }
        return connection;
    }

    /**
     * @param connection
     */
    public void disconnect(Connection connection) {
        if(manager.destroy(connection.address, connection.port))
            dispatcher.onEvent(new DisconnectionEvent(connection));
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void send(Connection connection, byte[] data, int channel) {
        send(connection, data, 0, data.length, channel);
    }

    public void send(Connection connection, byte[] data, int offset, int length, int channel) {
        requireRunning();
        byte[] buffer = new byte[length + 4 - offset];
        Serial.write(buffer, 0, channel); //channel overhead
        System.arraycopy(data, offset, buffer, 4, data.length); // copy data to the buffer
        try {
            //Send the generated byte array
            socket.send(new DatagramPacket(buffer, 0, buffer.length, connection.address, connection.port));
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }

    public UNetDispatcher getDispatcher() {
        return dispatcher;
    }
}