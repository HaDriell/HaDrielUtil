package fr.hadriel.ecs;

import java.util.Objects;

public class DelegateEntityProcessor extends EntitySystem {

    private final IEntityProcessor delegate;

    public DelegateEntityProcessor(Profile.Builder profiler, IEntityProcessor delegate) {
        super(profiler);
        this.delegate = Objects.requireNonNull(delegate);
    }

    public void onEntityRegistered(long id) {
        delegate.onEntityRegistered(id);
    }

    public void begin() {
        delegate.begin();
    }

    public void process(long id, IEntityManager manager, float deltaTime) {
        delegate.process(id, manager, deltaTime);
    }

    public void end() {
        delegate.end();
    }

    public void onEntityUnregistered(long id) {
        delegate.onEntityUnregistered(id);
    }
}
