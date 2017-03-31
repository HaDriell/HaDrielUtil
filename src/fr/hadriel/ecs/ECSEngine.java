package fr.hadriel.ecs;

import fr.hadriel.ecs.events.*;
import fr.hadriel.event.EventListener;
import fr.hadriel.event.IEvent;

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
    private List<IEvent> queue;
    private ECSEventDispatcher dispatcher;
    private List<EntitySystem> systems;


    private Lock entitiesAccess;
    private Lock queueAccess;
    private Lock systemAccess;

    public ECSEngine() {
        this.queue = new ArrayList<>(1024);
        this.entities = new HashMap<>();
        this.dispatcher = new ECSEventDispatcher();
        this.systems = new ArrayList<>();
        this.entitiesAccess = new ReentrantLock();
        this.queueAccess = new ReentrantLock();
        this.systemAccess = new ReentrantLock();

        //Core Listener initialization
        EventListener coreListener = new EventListener();
        coreListener.setEventHandler(ComponentModificationEvent.class, this::handle);
        coreListener.setEventHandler(EntityCreatedEvent.class, this::handle);
        coreListener.setEventHandler(EntityDestroyedEvent.class, this::handle);
        addEventListener(coreListener);
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

    public void addEventListener(EventListener listener) {
        dispatcher.addEventListener(listener);
    }

    public void removeEventListener(EventListener listener) {
        dispatcher.removeEventListener(listener);
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
            for(IEvent event : queue)
                dispatcher.dispatch(event);
            queue.clear();
            queueAccess.unlock();

            //System Update (generates Events)
            system.update(this, delta);
        }
        systemAccess.unlock();
    }

    private void submit(IEvent event) {
        if(event == null)
            return;
        queueAccess.lock();
        queue.add(event);
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

    private void handle(EntityCreatedEvent event) {
        entitiesAccess.lock();
        EntityData old = entities.get(event.id);
        if(old != null) //insert EntityDestroyedEvent
            dispatcher.dispatch(new EntityDestroyedEvent(event.id));
        //Entity creation
        EntityData data;
        entities.put(event.id, data = new EntityData());
        map(event.id, data);
        entitiesAccess.unlock();
    }

    private void handle(EntityDestroyedEvent event) {
        entitiesAccess.lock();
        EntityData data = entities.remove(event.id);
        //Check that the destroy action really happened
        if(data != null) {
            //make sure all remaining components are destroyed
            for(Map.Entry<String, Object> comp : data)
                dispatcher.dispatch(new ComponentDestroyedEvent(event.id, comp.getKey(), comp.getValue()));
            //unmap the destroyed entity
            unmap(event.id);
        }
        entitiesAccess.unlock();
    }

    private void handle(ComponentModificationEvent event) {
        entitiesAccess.lock();
        EntityData data = entities.get(event.id);
        if(data != null) {
            //Modification
            Object component = data.setComponent(event.name, event.component);
            //Event Forward
            if(component != null && event.component != null && component.getClass() == event.component.getClass())
                return; // value update (for constant components update)

            if(component != null && event.component != null && component.getClass() != event.component.getClass())
                dispatcher.dispatch(new ComponentTypeChangedEvent(event.id, event.name, event.component));
            else if (component == null)
                dispatcher.dispatch(new ComponentCreatedEvent(event.id, event.name, event.component));
            else if (event.component == null)
                dispatcher.dispatch(new ComponentDestroyedEvent(event.id, event.name, event.component));
            map(event.id, data);
        }
        entitiesAccess.unlock();
    }

    /* Private unlistenable Event for core Listener handling */
    private static class ComponentModificationEvent implements IEvent {
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