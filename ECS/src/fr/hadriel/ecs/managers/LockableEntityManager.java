package fr.hadriel.ecs.managers;

import fr.hadriel.ecs.IEntityManager;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockableEntityManager implements IEntityManager {

    private final IEntityManager manager;
    private final Lock lock;

    public LockableEntityManager(IEntityManager manager) {
        this.manager = manager;
        this.lock = new ReentrantLock();
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public long create() {
        lock.lock();
        long id = manager.create();
        lock.unlock();
        return id;
    }

    public void destroy(long id) {
        lock.lock();
        manager.destroy(id);
        lock.unlock();
    }

    public boolean exists(long id) {
        lock.lock();
        boolean result = manager.exists(id);
        lock.unlock();
        return result;
    }

    public <T> boolean hasComponent(long id, Class<T> type) {
        lock.lock();
        boolean result = manager.hasComponent(id, type);
        lock.unlock();
        return result;
    }

    public <T> void setComponent(long id, T component) {
        lock.lock();
        manager.setComponent(id, component);
        lock.unlock();
    }

    public <T> boolean removeComponent(long id, Class<T> type) {
        lock.lock();
        boolean result = manager.removeComponent(id, type);
        lock.unlock();
        return result;
    }

    public <T> T getComponent(long id, Class<T> type) {
        lock.lock();
        T component = manager.getComponent(id, type);
        lock.unlock();
        return component;
    }

    public Map<Class, Object> getComponents(long id) {
        lock.lock();
        Map<Class, Object> components = manager.getComponents(id);
        lock.unlock();
        return components;
    }
}
