package fr.hadriel.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Profile {

    private List<IAspect> aspects;

    private Profile(List<IAspect> aspects) {
        this.aspects = new ArrayList<>(Objects.requireNonNull(aspects));
    }

    public boolean validate(long id, IEntityManager manager) {
        for(IAspect aspect : aspects)
            if(!aspect.validate(id, manager))
                return false;
        return true;
    }

    public static Builder any() {
        return new Builder();
    }

    public static Builder include(Class<?>... types) {
        return new Builder().include(types);
    }

    public static Builder exclude(Class<?>... types) {
        return new Builder().exclude(types);
    }

    public static class Builder {
        private List<IAspect> aspects;

        public Builder() {
            this.aspects = new ArrayList<>();
        }

        public Builder include(Class<?>... types) {
            aspects.add(IAspect.Include(types));
            return this;
        }

        public Builder exclude(Class<?>... types) {
            aspects.add(IAspect.Exclude(types));
            return this;
        }

        public Profile build() {
            return new Profile(aspects);
        }
    }

    @FunctionalInterface
    public static interface IAspect {
        public boolean validate(long id, IEntityManager manager);

        public static IAspect Include(Class<?>... types) {
            return (id, manager) -> Arrays.stream(types).allMatch(type -> manager.hasComponent(id, type));
        }

        public static IAspect Exclude(Class<?>... types) {
            return (id, manager) -> Arrays.stream(types).noneMatch(type -> manager.hasComponent(id, type));
        }
    }
}