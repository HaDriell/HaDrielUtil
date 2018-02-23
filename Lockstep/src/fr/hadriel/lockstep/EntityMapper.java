package fr.hadriel.lockstep;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EntityMapper {
    private final EntityProfile profile;

    private List<Entity> entities;

    public EntityMapper(EntityProfile profile) {
        this.profile = Objects.requireNonNull(profile);
        this.entities = new ArrayList<>();
    }

    public EntityMapper(EntityProfile.Builder builder) {
        this(builder.build());
    }

    public void map(Entity entity) {
        boolean ok = !entity.isDestroyed() && profile.validate(entity);
        boolean mapped = entities.contains(entity);

        if (ok && !mapped) entities.add(entity);
        if (!ok && mapped) entities.remove(entity);
    }

    public void unmap(Entity entity) {
        entities.remove(entity);
    }

    public Stream<Entity> entities() {
        return entities.stream();
    }
}