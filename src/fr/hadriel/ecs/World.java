package fr.hadriel.ecs;


/**
 * Created by glathuiliere setOn 21/11/2016.
 */
public class World {

    private ProcessorManager processorManager;
    private EntityManager entityManager;

    public World() {
        this.processorManager = new ProcessorManager();
        this.entityManager = new EntityManager();
    }

    public void update(float delta) {
        entityManager.update();
        processorManager.update(entityManager.getEntities(), delta);
    }

    public void addEntity(long id) {
        entityManager.add(id);
    }

    public void removeEntity(long id) {
        entityManager.remove(id);
    }

    public void addComponent(long id, Component component) {
        Entity e = entityManager.get(id);
        if(e == null) return;
        e.add(component);
    }

    public void removeComponent(long id, Component component) {
        Entity e = entityManager.get(id);
        if(e == null) return;
        e.remove(component);
    }

    public void addProcessor(EntityProcessor processor) {
        this.processorManager.add(processor);
    }

    public void removeProcessor(EntityProcessor processor) {
        this.processorManager.remove(processor);
    }
}