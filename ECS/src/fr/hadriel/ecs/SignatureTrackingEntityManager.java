package fr.hadriel.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SignatureTrackingEntityManager implements IEntityManager {

    private final IEntityManager manager;

    private final List<Long> editedEntities;

    public SignatureTrackingEntityManager(IEntityManager manager) {
        this.manager = manager;
        this.editedEntities = new ArrayList<>(512);
    }

    public List<Long> getEditedEntities() {
        return editedEntities;
    }

    public synchronized void clearEditedEntities() {
        editedEntities.clear();
    }

    private synchronized void notifyEdition(long id) {
        if(!editedEntities.contains(id)) {
            editedEntities.add(id);
        }
    }

    public synchronized long create() {
        long id = manager.create();
        notifyEdition(id);
        return id;
    }

    public synchronized void destroy(long id) {
        manager.destroy(id);
        notifyEdition(id);
    }

    public synchronized boolean exists(long id) {
        return manager.exists(id);
    }

    public synchronized <T> boolean hasComponent(long id, Class<T> type) {
        return manager.hasComponent(id, type);
    }

    public synchronized <T> void setComponent(long id, T component) {
        if(!manager.hasComponent(id, component.getClass()))
            notifyEdition(id);
        manager.setComponent(id, component);
    }

    public synchronized <T> boolean removeComponent(long id, Class<T> type) {
        if(manager.removeComponent(id, type)) {
            notifyEdition(id);
            return true;
        }
        return false;
    }

    public synchronized <T> T getComponent(long id, Class<T> type) {
        return manager.getComponent(id, type);
    }

    public synchronized Map<Class, Object> getComponents(long id) {
        return manager.getComponents(id);
    }
}