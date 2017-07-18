package fr.hadriel.main.ecs;

import fr.hadriel.main.ecs.events.*;
import fr.hadriel.main.event.ForwardEventListener;
import fr.hadriel.main.event.IEvent;
import fr.hadriel.main.event.IEventListener;
import fr.hadriel.main.event.MultiEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public class ECSEngine implements EntityDataManager {

    private Map<Long, EntityData> entities;
    private List<EntitySystem> systems;

    private ForwardEventListener dispatcher;
    private List<ECSEvent> queue;


    private Lock entitiesAccess;
    private Lock queueAccess;
    private Lock systemAccess;

    public ECSEngine() {
        this.queue = new ArrayList<>(1024);
        this.entities = new HashMap<>();
        this.systems = new ArrayList<>();
        this.entitiesAccess = new ReentrantLock(); // should be surrounding any 'entities' concrete operations.
        this.queueAccess = new ReentrantLock();
        this.systemAccess = new ReentrantLock();
        this.dispatcher = new ForwardEventListener();

        //Core Listener initialization (binds the default Handles first)
        MultiEventListener coreListener = new MultiEventListener();
        coreListener.setEventHandler(ComponentModificationEvent.class, this::handle);
        coreListener.setEventHandler(EntityCreatedEvent.class, this::handle);
        coreListener.setEventHandler(EntityDestroyedEvent.class, this::handle);
        dispatcher.addEventListener(coreListener);
    }

    public void addSystem(EntitySystem system) {
        if(system == null)
            return;
        systemAccess.lock();
        systems.add(system);
        systemAccess.unlock();
    }

    public void removeSystem(EntitySystem system) {
        if(system == null)
            return;
        systemAccess.lock();
        systems.remove(system);
        systemAccess.unlock();
    }

    public void addEventListener(IEventListener listener) {
        if(listener != null)
            dispatcher.addEventListener(listener);
    }

    public void removeEventListener(IEventListener listener) {
        if(listener != null)
            dispatcher.removeEventListener(listener);
    }

    private void dispatch(ECSEvent event) {
        dispatcher.onEvent(event);
    }

    public List<ECSEvent> snapshot() {
        entitiesAccess.lock();
        ArrayList<ECSEvent> events = new ArrayList<>(entities.size()); // preallocation (undersized but already quite big)
        for(Map.Entry<Long, EntityData> entity : entities.entrySet()) {
            events.add(new EntityCreatedEvent(entity.getKey()));
            for(Map.Entry<String, Object> component : entity.getValue()) {
                events.add(new ComponentModificationEvent(entity.getKey(), component.getKey(), component.getValue()));
            }
        }
        entitiesAccess.unlock();
        queueAccess.lock();
        events.addAll(queue);
        queueAccess.unlock();
        events.trimToSize();
        return events;
    }

    private void map(long id, EntityData data) {
        systemAccess.lock();
        for(EntitySystem system : systems)
            system.map(id, data);
        systemAccess.unlock();
    }

    private void unmap(long id) {
        systemAccess.lock();
        for(EntitySystem system : systems)
            system.unmap(id);
        systemAccess.unlock();
    }

    public void update(float delta) {
        systemAccess.lock();
        for(EntitySystem system : systems) {

            //Resolve all queued Events
            queueAccess.lock();
            for(ECSEvent event : queue)
                dispatch(event);
            queue.clear();
            queueAccess.unlock();

            //System Update (generates Events)
            system.update(this, delta);
        }
        systemAccess.unlock();
    }

    public void submit(ECSEvent event) {
        if(event == null)
            return;
        queueAccess.lock();
        queue.add(event);
        queueAccess.unlock();
    }

    public void submitAll(List<ECSEvent> events) {
        if(events == null)
            return;
        queueAccess.lock();
        for(ECSEvent event : events)
            if(event != null) queue.add(event);
        queueAccess.unlock();
    }

    public void createEntity(long id) {
        submit(new EntityCreatedEvent(id));
    }

    public void destroyEntity(long id) {
        submit(new EntityDestroyedEvent(id));
    }

    public Object getComponent(long id, String name) {
        entitiesAccess.lock();
        EntityData data = entities.get(id);
        entitiesAccess.unlock();
        return data != null ? data.getComponent(name) : null;
    }

    public <T> T getComponent(long id, String name, Class<T> type) {
        Object component = getComponent(id, name);
        return type.isInstance(component) ? type.cast(component) : null;
    }

    public void setComponent(long id, String name, Object component) {
        submit(new ComponentModificationEvent(id, name, component));
    }

    private IEvent handle(EntityCreatedEvent event) {
        entitiesAccess.lock();
        EntityData old = entities.get(event.id);
        if(old != null) //insert EntityDestroyedEvent
            dispatch(new EntityDestroyedEvent(event.id));
        //Entity creation
        EntityData data;
        entities.put(event.id, data = new EntityData());
        map(event.id, data);
        entitiesAccess.unlock();
        return event;
    }

    private IEvent handle(EntityDestroyedEvent event) {
        entitiesAccess.lock();
        EntityData data = entities.remove(event.id);
        //Check that the destroy action really happened
        if(data != null) {
            //make sure all remaining components are destroyed
            for(Map.Entry<String, Object> comp : data)
                dispatch(new ComponentDestroyedEvent(event.id, comp.getKey(), comp.getValue()));
            //unmap the destroyed entity
            unmap(event.id);
        }
        entitiesAccess.unlock();
        return event;
    }

    private IEvent handle(ComponentModificationEvent event) {
        entitiesAccess.lock();
        EntityData data = entities.get(event.id);
        if(data != null) {
            //Modification
            Object component = data.setComponent(event.name, event.component);
            //Event Forward
            if(component != null && event.component != null && component.getClass() == event.component.getClass())
                return null; // value update (for constant components update)

            if(component != null && event.component != null && component.getClass() != event.component.getClass())
                dispatch(new ComponentTypeChangedEvent(event.id, event.name, event.component));
            else if (component == null)
                dispatch(new ComponentCreatedEvent(event.id, event.name, event.component));
            else if (event.component == null)
                dispatch(new ComponentDestroyedEvent(event.id, event.name, event.component));
            map(event.id, data);
        }
        entitiesAccess.unlock();
        return event;
    }

    /* Private unlistenable Event for core Listener handling */
    private static class ComponentModificationEvent implements ECSEvent {
        public final long id;
        public final String name;
        public final Object component;

        public ComponentModificationEvent(long id, String name, Object component) {
            this.id = id;
            this.name = name;
            this.component = component;
        }
    }
}