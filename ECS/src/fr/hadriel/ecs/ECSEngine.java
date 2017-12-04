package fr.hadriel.ecs;

import fr.hadriel.ecs.managers.SignatureTrackingEntityManager;
import fr.hadriel.ecs.managers.LockableEntityManager;
import fr.hadriel.ecs.managers.DirectEntityManager;

import java.util.ArrayList;
import java.util.List;

public class ECSEngine {

    private List<EntitySystem> systems;

    private DirectEntityManager raw;
    private SignatureTrackingEntityManager intern;
    private LockableEntityManager extern;

    public ECSEngine() {
        this.systems = new ArrayList<>();
        this.raw = new DirectEntityManager();
        this.intern = new SignatureTrackingEntityManager(raw);
        this.extern = new LockableEntityManager(intern);
    }

    public int count() {
        extern.lock();
        int count = (int) raw.entities().count();
        extern.unlock();
        return count;
    }

    public synchronized void addEntitySystem(EntitySystem system) {
        if(systems.contains(system))
            return;
        systems.add(system);
        raw.entities().forEach(id -> system.onEntitySignatureChanged(id, intern)); // virtual update for runtime setup
    }

    public synchronized void removeEntitySystem(EntitySystem system) {
        systems.remove(system);
        raw.entities().forEach(id -> system.onEntityDestroyed(id)); // virtual destruction for runtime cleaning
    }

    public EntitySystem addEntitySystem(Profile.Builder profiler, IEntityProcessor processor) {
        EntitySystem generatedSystem = new DelegateEntityProcessor(profiler, processor);
        addEntitySystem(generatedSystem);
        return generatedSystem;
    }

    private void notifyEntityModifications() {
        if(intern.getEditedEntities().size() > 0) {
            for (long id : intern.getEditedEntities())
                systems.forEach(s -> s.onEntitySignatureChanged(id, intern));
            intern.clearEditedEntities();
        }
    }

    public synchronized void update(float deltaTime) {
        extern.lock();
        for(EntitySystem system : systems) {
            notifyEntityModifications(); // clear modifications before system-scale update
            system.update(intern, deltaTime);
        }
        extern.unlock();
    }

    public IEntityManager entities() {
        return extern;
    }
}
