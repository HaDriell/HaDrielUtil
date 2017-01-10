package fr.hadriel.ecs;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere setOn 21/11/2016.
 */
public class World {

    private EntityManager entityManager;
    private List<EntityProcessor> processors;

    public World() {
        this.processors = new ArrayList<>();
        this.entityManager = new EntityManager();
    }

    public synchronized void update(float delta) {
        entityManager.sync();
        List<Entity> entities = entityManager.getEntities();
        for(EntityProcessor p : processors) {
            for(Entity e : entities) {
                p.update(e, delta);
            }
        }
    }

    public void createEntity(Entity entity) {
        entityManager.create(entity);
    }

    public void destroyEntity(Entity entity) {
        entityManager.destroy(entity);
    }

    public void createEntity(long id) {
        entityManager.create(id);
    }

    public void destroyEntity(long id) {
        entityManager.destroy(id);
    }

    public Entity getEntity(long id) {
        return entityManager.get(id);
    }

    public synchronized void addProcessor(EntityProcessor processor) {
        processors.add(processor);
    }

    public synchronized void removeProcessor(EntityProcessor processor) {
        processors.remove(processor);
    }

    public <T extends EntityProcessor> T getProcessor(Class<T> type) {
        for(EntityProcessor p : processors) {
            if(p.getClass() == type) return type.cast(p);
        }
        return null;
    }
}