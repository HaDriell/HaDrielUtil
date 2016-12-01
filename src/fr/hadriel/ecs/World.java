package fr.hadriel.ecs;


import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.events.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 21/11/2016.
 */
public class World implements IEventListener {

    private ProcessorManager processorManager;
    private EntityManager entityManager;

    public World() {
        this.processorManager = new ProcessorManager();
        this.entityManager = new EntityManager();
    }

    private boolean onEntityCreated(EntityCreatedEvent event) {
        entityManager.add(new Entity(event.id));
        return true;
    }

    private boolean onEntityDestroyed(EntityDestroyedEvent event) {
        entityManager.remove(event.id);
        return true;
    }

    private boolean onComponentCreated(ComponentCreatedEvent event) {
        Entity entity = entityManager.get(event.id);
        if(entity == null) return false;
        entity.add(event.componentClass);
        return true;
    }

    private boolean onComponentUpdated(ComponentUpdatedEvent event) {
        Entity entity = entityManager.get(event.id);
        if(entity == null) return false;
        Component component = entity.get(event.component.getClass());
        if(component == null) return false;
        component.set(event.component);
        return true;
    }

    private boolean onComponentDestroyed(ComponentDestroyedEvent event) {
        Entity entity = entityManager.get(event.id);
        if(entity == null) return false;
        entity.remove(event.componentClass);
        return true;
    }

    public boolean onEvent(IEvent event) {
        boolean result = false;
        if(IEvent.dispatch(event, EntityCreatedEvent.class, this::onEntityCreated)) result = true;
        if(IEvent.dispatch(event, EntityDestroyedEvent.class, this::onEntityDestroyed)) result = true;
        if(IEvent.dispatch(event, ComponentCreatedEvent.class, this::onComponentCreated)) result = true;
        if(IEvent.dispatch(event, ComponentUpdatedEvent.class, this::onComponentUpdated)) result = true;
        if(IEvent.dispatch(event, ComponentDestroyedEvent.class, this::onComponentDestroyed)) result = true;
        return result;
    }

    public void addProcessor(EntityProcessor processor) {
        this.processorManager.add(processor);
    }

    public void removeProcessor(EntityProcessor processor) {
        this.processorManager.remove(processor);
    }

    public void createEntity(long id) {
        onEvent(new EntityCreatedEvent(id));
    }

    public void deleteEntity(long id) {
        onEvent(new EntityDestroyedEvent(id));
    }

    public void addComponent(long id, Class<? extends Component> clazz) {
        onEvent(new ComponentCreatedEvent(id, clazz));
    }

    public void removeComponent(long id, Class<? extends Component> clazz) {
        onEvent(new ComponentDestroyedEvent(id, clazz));
    }

    public void updateComponent(long id, Component component) {
        onEvent(new ComponentUpdatedEvent(id, component));
    }
}