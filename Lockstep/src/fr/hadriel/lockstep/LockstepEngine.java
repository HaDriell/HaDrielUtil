package fr.hadriel.lockstep;

import java.util.*;
import java.util.stream.Stream;

public strictfp class LockstepEngine {

    private Map<Long, Entity> entities; // fast lookup map of entities
    private long sequence;

    private List<ILockstepExtension> extensions;

    public LockstepEngine() {
        this.entities = new HashMap<>();
        this.extensions = new ArrayList<>();
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
        extensions.forEach(e -> e.step(deltaTime));
    }

    private void notifyEntityModifications() {
        //Notify Mappers
        entities.values().stream().filter(Entity::isUnstable).forEach(entity -> extensions.forEach(extension -> {
            if(entity.isDestroyed()) extension.entityDestroyed(entity);
            else if(entity.isCreated()) extension.entityCreated(entity);
            else if(entity.isModified()) extension.entityModified(entity);
        }));
        //Clear Flags
        Iterator<Entity> it = entities.values().iterator();
        while(it.hasNext()) {
            Entity e = it.next();
            if(e.isDestroyed()) it.remove();
            else e.clearFlags();
        }
    }

    public void addExtension(ILockstepExtension extension) {
        if(extensions.contains(extension)) return;
        extensions.add(extension);
        extension.load();
    }

    public void removeExtension(ILockstepExtension extension) {
        if(!extensions.remove(extension)) return;
        extension.unload();
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
        extensions.forEach(ILockstepExtension::unload);
    }
}