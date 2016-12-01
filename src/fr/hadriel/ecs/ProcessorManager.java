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

    public void forEach(Consumer<EntityProcessor> consumer) {
        lock.lock();
        processors.forEach(consumer);
        lock.unlock();
    }
}