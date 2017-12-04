package fr.hadriel.ecs;

import java.util.ArrayList;
import java.util.List;

public abstract class EntitySystem implements IEntityProcessor {

    private final Profile profile;
    private final List<Long> entities;

    protected EntitySystem(Profile.Builder profiler) {
        this.profile = profiler.build();
        this.entities = new ArrayList<>(256);
    }

    private void unregisterEntity(long id) {
        if(entities.remove(id)) {
            onEntityUnregistered(id);
        }
    }

    private void registerEntity(long id) {
        if(!entities.contains(id)) {
            entities.add(id);
            onEntityRegistered(id);
        }
    }

    public void onEntitySignatureChanged(long id, IEntityManager manager) {
        if(profile.validate(id, manager))
            registerEntity(id);
        else
            unregisterEntity(id);
    }

    public void onEntityDestroyed(long id) {
        unregisterEntity(id);
    }

    public void update(IEntityManager manager, float deltaTime) {
        begin();
        entities.parallelStream().forEach(id -> process(id, manager, deltaTime));
        end();
    }
}