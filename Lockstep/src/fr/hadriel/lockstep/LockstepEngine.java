package fr.hadriel.lockstep;

import java.util.*;
import java.util.stream.Stream;

public strictfp class LockstepEngine {

    private Map<Long, Entity> entities; // fast lookup map of entities
    private long sequence;

    private List<ILockstepSystem> systems;

    public LockstepEngine() {
        this.entities = new HashMap<>();
        this.systems = new ArrayList<>();
    }

    public Entity newEntity(IComponent... components) {
        sequence++;
        sequence &= Long.MAX_VALUE;
        Entity entity = new Entity(sequence, components);

        if(entities.put(entity.id, entity) != null)
            throw new RuntimeException("Duplicate ID in entity base");
        return entity;
    }

    public void step(float deltaTime) {
        notifyEntityModifications();
        systems.forEach(e -> e.step(deltaTime));
    }

    private void notifyEntityModifications() {
        //Notify Mappers
        entities.values().stream()
                .filter(Entity::isUnstable)
                .forEach(entity -> systems.forEach(system -> {
            if(entity.isDestroyed()) system.entityDestroyed(entity);
            else if(entity.isCreated()) system.entityCreated(entity);
            else if(entity.isModified()) system.entityModified(entity);
        }));
        //Clear Flags & Clean-up Entities
        Iterator<Entity> it = entities.values().iterator();
        while(it.hasNext()) {
            Entity e = it.next();
            if(e.isDestroyed()) it.remove();
            else e.clearFlags();
        }
    }

    public void addSystem(ILockstepSystem system) {
        if(systems.contains(system)) return;
        systems.add(system);
        system.load();
    }

    public void removeSystem(ILockstepSystem system) {
        if(!systems.remove(system)) return;
        system.unload();
    }

    public Stream<Entity> entities() {
        return entities.values().stream();
    }

    public Stream<Entity> entities(EntityProfile.Builder profile) {
        return entities(profile.build());
    }

    public Stream<Entity> entities(EntityProfile profile) {
        return entities().filter(profile::validate);
    }

    public void finalize() {
        systems.forEach(ILockstepSystem::unload);
    }
}