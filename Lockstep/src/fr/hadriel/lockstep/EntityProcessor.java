package fr.hadriel.lockstep;

import java.util.Objects;
import java.util.stream.Stream;

public abstract class EntityProcessor implements ILockstepSystem {
    public static final boolean DEFAULT_USE_PARALLELISM = true;
    public static final boolean DEFAULT_IGNORE_DESTROYED_ENTITIES = true;

    private final EntityMapper mapper;
    private final boolean useParallelism;
    private final boolean ignoreDestroyedEntities;

    public EntityProcessor(EntityProfile.Builder profile) {
        this(profile, DEFAULT_USE_PARALLELISM, DEFAULT_IGNORE_DESTROYED_ENTITIES);
    }

    protected EntityProcessor(EntityProfile.Builder profile, boolean useParallelism, boolean ignoreDestroyedEntities) {
        this.mapper = new EntityMapper(Objects.requireNonNull(profile));
        this.useParallelism = useParallelism;
        this.ignoreDestroyedEntities = ignoreDestroyedEntities;
    }

    public void entityCreated(Entity entity) {
        mapper.map(entity);
    }

    public void entityModified(Entity entity) {
        mapper.map(entity);
    }

    public void entityDestroyed(Entity entity) {
        mapper.unmap(entity);
    }

    public final void step(float deltaTime) {
        begin(deltaTime);
        Stream<Entity> stream = mapper.entities();

        //Stream pipeline stuffing following the entity processor configuration
        if(ignoreDestroyedEntities) stream = stream.filter(e -> !e.isDestroyed());
        if(useParallelism) stream = stream.parallel();

        stream.forEach(e -> process(e, deltaTime));
        end(deltaTime);
    }

    protected abstract void begin(float deltaTime);
    protected abstract void process(Entity entity, float deltaTime);
    protected abstract void end(float deltaTime);
}