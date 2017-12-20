package fr.hadriel.ecs;

import fr.hadriel.ecs.EntityData;
import fr.hadriel.ecs.IEntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The most basic & Direct implementation of an ECS EntityManager.
 * Handles creation / deletion / modifications / queries about entities and components.
 * Doesn't have multithreading safety
 */
public class DirectEntityManager implements IEntityManager {

    private Map<Long, EntityData> entities;
    private long generator;

    public DirectEntityManager() {
        this.entities = new HashMap<>();
        this.generator = 0;
    }

    public long create() {
        long id = generator++;
        entities.put(id, new EntityData());
        return id;
    }

    public void destroy(long id) {
        entities.remove(id);
    }

    public boolean exists(long id) {
        return entities.get(id) != null;
    }

    public <T> boolean hasComponent(long id, Class<T> type) {
        EntityData data = entities.get(id);
        return data != null && data.hasComponent(type);
    }


    public <T> void setComponent(long id, T component) {
        EntityData data = entities.get(id);
        if(data != null) data.setComponent(component);
    }

    public <T> boolean removeComponent(long id, Class<T> type) {
        EntityData data = entities.get(id);
        return data != null && data.removeComponent(type) != null;
    }

    public Map<Class, Object> getComponents(long id) {
        EntityData data = entities.get(id);
        return data == null ? null : data.getComponents();
    }

    public <T> T getComponent(long id, Class<T> type) {
        EntityData data = entities.get(id);
        return data == null ? null : data.getComponent(type);
    }

    public Stream<Long> entities() {
        return entities.entrySet().stream().map(Map.Entry::getKey);
    }
}
