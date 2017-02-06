package fr.hadriel.entities;


import fr.hadriel.entities.events.*;
import fr.hadriel.event.Event;
import fr.hadriel.event.IEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by glathuiliere on 21/11/2016.
 */
public class World {

    //World Data
    private List<Entity> entities;
    private List<EntityProcessor> processors;

    //World Events
    private List<IEventListener> listeners;
    private Queue<Event> queue;
    private Lock lock;

    public World() {
        this.entities = new ArrayList<>();
        this.processors = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
    }

    /**
     * Resolves all events queued since last polling
     */
    public synchronized void pollEvents() {
        lock.lock();
        while(!queue.isEmpty()) {
            Event event = queue.poll();
            if(event == null) continue;

            //Entity Management
            Event.dispatch(event, CreateEntityEvent.class, this::onCreateEntity);
            Event.dispatch(event, DestroyEntityEvent.class, this::onDestroyEntity);
            //Property Management
            Event.dispatch(event, SetPropertyEvent.class, this::onSetProperty);
            Event.dispatch(event, DestroyPropertyEvent.class, this::onDestroyProperty);

            //Listener forwarding
            for(IEventListener listener : listeners) {
                listener.onEvent(event);
            }
        }
        lock.unlock();
    }

    public synchronized void update(float delta) {
        for (EntityProcessor p : processors) {
            p.beforeUpdate();
            for (Entity e : entities) {
                p.update(this, e, delta);
            }
            p.afterUpdate();
        }
    }

    private Entity getEntity(long id) {
        for(Entity e : entities)
            if(e.id == id)
                return e;
        return null;
    }

    private void onCreateEntity(CreateEntityEvent event) {
        System.out.println("Creating Entity " + event.id);
        Entity e = getEntity(event.id);
        if(e == null) {
            e = new Entity(event.id);
            entities.add(e);
        } else {
            e.clear();
        }
    }

    private void onDestroyEntity(DestroyEntityEvent event) {
        System.out.println("Destroying Entity " + event.id);
        Entity e = getEntity(event.id);
        if(e != null)
            entities.remove(e);
    }

    private void onSetProperty(SetPropertyEvent event) {
        System.out.println("Setting Property " + event.name + " on Entity " + event.id);
        Entity e = getEntity(event.id);
        if(e != null) {
            e.set(event.name, event.value);
        }
    }

    private void onDestroyProperty(DestroyPropertyEvent event) {
        System.out.println("Destroying Property " + event.name + " on Entity " + event.id);
        Entity e = getEntity(event.id);
        if(e != null) {
            entities.remove(e);
        }
    }

    /* LISTENER API */

    public synchronized void addWorldListener(IEventListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeWorldListener(IEventListener listener) {
        listeners.remove(listener);
    }

    /* WORLD API */

    public synchronized void addProcessor(EntityProcessor processor) {
        processors.add(processor);
    }

    public synchronized void removeProcessor(EntityProcessor processor) {
        processors.remove(processor);
    }

    public void createEntity(long id) {
        lock.lock();
        queue.add(new CreateEntityEvent(id));
        lock.unlock();
    }

    public void destroyEntity(long id) {
        lock.lock();
        queue.add(new DestroyEntityEvent(id));
        lock.unlock();
    }

    public void setProperty(long id, String name, Object value) {
        lock.lock();
        queue.add(new SetPropertyEvent(id, name, value));
        lock.unlock();
    }

    public void destroyProperty(long id, String name) {
        lock.lock();
        queue.add(new DestroyPropertyEvent(id, name));
        lock.unlock();
    }
}