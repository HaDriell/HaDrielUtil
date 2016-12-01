package fr.hadriel.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Created by HaDriel on 24/11/2016.
 */
public class EntityManager {

    private List<Entity> entities;
    private Lock lock = new ReentrantLock();

    public EntityManager() {
        this.entities = new ArrayList<>();
    }

    public Entity get(long id) {
        Entity result = null;
        lock.lock();
        for(Entity e : entities) {
            if(e.id == id) {
                result = e;
                break;
            }
        }
        lock.unlock();
        return result;
    }

    public void add(Entity e) {
        remove(get(e.id));
        lock.lock();
        entities.add(e);
        lock.unlock();
    }

    public void remove(long id) {
        remove(get(id));
    }

    public void remove(Entity e) {
        lock.lock();
        entities.remove(e);
        lock.unlock();
    }

    public void clear() {
        lock.lock();
        entities.clear();
        lock.unlock();
    }

    public void forEach(Consumer<Entity> consumer) {
        lock.lock();
        entities.forEach(consumer);
        lock.unlock();
    }
}
