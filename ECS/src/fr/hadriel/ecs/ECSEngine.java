package fr.hadriel.ecs;

import java.util.ArrayList;
import java.util.List;

public class ECSEngine {

    private List<EntitySystem> systems;

    private DirectEntityManager rawManager;
    private SignatureTrackingEntityManager internalManager;
    private LockableEntityManager publicManager;

    public ECSEngine() {
        this.systems = new ArrayList<>();
        this.rawManager = new DirectEntityManager();
        this.internalManager = new SignatureTrackingEntityManager(rawManager);
        this.publicManager = new LockableEntityManager(internalManager);
    }

    public int count() {
        publicManager.lock();
        int count = (int) rawManager.entities().count();
        publicManager.unlock();
        return count;
    }

    public synchronized void addEntitySystem(EntitySystem system) {
        if(systems.contains(system))
            return;
        systems.add(system);
        rawManager.entities().forEach(id -> system.onEntitySignatureChanged(id, internalManager)); // virtual update for runtime setup
    }

    public synchronized void removeEntitySystem(EntitySystem system) {
        systems.remove(system);
        rawManager.entities().forEach(id -> system.onEntityDestroyed(id)); // virtual destruction for runtime cleaning
    }

    public EntitySystem addEntitySystem(Profile.Builder profiler, IEntityProcessor processor) {
        EntitySystem generatedSystem = new DelegateEntityProcessor(profiler, processor);
        addEntitySystem(generatedSystem);
        return generatedSystem;
    }

    private void notifyEntityModifications() {
        if(internalManager.getEditedEntities().size() > 0) {
            for (long id : internalManager.getEditedEntities())
                systems.forEach(s -> s.onEntitySignatureChanged(id, internalManager));
            internalManager.clearEditedEntities();
        }
    }

    public synchronized void update(float deltaTime) {
        publicManager.lock();
        for(EntitySystem system : systems) {
            notifyEntityModifications(); // clear modifications before system-scale update
            system.update(internalManager, deltaTime);
        }
        publicManager.unlock();
    }

    public IEntityManager entities() {
        return publicManager;
    }
}