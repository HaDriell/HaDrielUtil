package fr.hadriel.ecs.filter;


import fr.hadriel.ecs.EntityData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * Created by glathuiliere on 28/03/2017.
 */
public class EntityMapper {

    private final IFilter[] filters;
    private final Lock lock;
    private final List<Long> entities;

    public EntityMapper(IFilter... filters) {
        if(filters.length == 0)
            throw new IllegalArgumentException("EntityMapper must have at least 1 filter for mapping.");
        this.filters = filters;
        this.entities = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public int count() {
        return entities.size();
    }

    public Stream<Long> getEntityStream() {
        return entities.stream();
    }

    public void map(Long id, EntityData data) {
        if(id == null) throw new IllegalArgumentException("id cannot be null");
        lock.lock();
        boolean accept = Filters.accept(filters, data);
        boolean mapped = entities.contains(id);
        if(accept && !mapped) entities.add(id);
        if(!accept && mapped) entities.remove(id);
        lock.unlock();
    }

    public void unmap(Long id) {
        lock.lock();
        entities.remove(id);
        lock.unlock();
    }
}