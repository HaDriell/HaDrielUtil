package fr.hadriel.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Created by HaDriel on 24/11/2016.
 */
public class ProcessorManager {

    private List<EntityProcessor> processors;
    private Lock lock = new ReentrantLock();

    public ProcessorManager() {
        this.processors = new ArrayList<>();
    }

    public void add(EntityProcessor processor) {
        lock.lock();
        processors.add(processor);
        lock.unlock();
    }

    public void remove(EntityProcessor processor) {
        lock.lock();
        processors.remove(processor);
        lock.unlock();
    }

    public void update(List<Entity> entities, float delta) {
        lock.lock();
        for(EntityProcessor processor : processors) {
            for(Entity e : entities) {
                if (processor.accept(e)) processor.update(e, delta);
            }
        }
        lock.unlock();
    }
}