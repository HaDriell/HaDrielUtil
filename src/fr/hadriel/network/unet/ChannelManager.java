package fr.hadriel.network.unet;

import fr.hadriel.events.DataEvent;
import fr.hadriel.events.UpdateEvent;
import fr.hadriel.events.unet.ConnectionEvent;
import fr.hadriel.events.unet.DisconnectionEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by glathuiliere on 01/09/2016.
 */
public class ChannelManager {

    private Map<Integer, UNetChannel> map;
    private Lock lock = new ReentrantLock();

    public ChannelManager() {
        this.map = new HashMap<>();
    }

    public void setChannel(int channel, UNetChannel handler) {
        lock.lock();
        if(handler == null) {
            map.remove(channel);
        } else {
            map.put(channel, handler);
        }
        lock.unlock();
    }

    public void clearChannels() {
        lock.lock();
        map.clear();
        lock.unlock();
    }

    public boolean fireDataEvent(DataEvent event) {
        boolean handled = false;
        lock.lock();
        UNetChannel result = map.get(event.channel);
        lock.unlock();
        if(result != null) {
            handled = result.onData(event);
        }
        return handled;
    }

    public boolean fireUpdateEvent(UpdateEvent event) {
        boolean handled = false;
        lock.lock();
        for (UNetChannel channel : map.values()) {
            handled = channel.onUpdate(event);
        }
        lock.unlock();
        return handled;
    }

    public boolean fireConnectionEvent(ConnectionEvent event) {
        boolean handled = false;
        lock.lock();
        for (UNetChannel channel : map.values()) {
            handled = channel.onConnection(event);
        }
        lock.unlock();
        return handled;
    }

    public boolean fireDisconnectionEvent(DisconnectionEvent event) {
        boolean handled = false;
        lock.lock();
        for (UNetChannel channel : map.values()) {
            handled = channel.onDisconnection(event);
        }
        lock.unlock();
        return handled;
    }
}