package fr.hadriel.lockstep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EntityProfile {

    private List<IAspect> aspects;

    private EntityProfile(List<IAspect> aspects) {
        this.aspects = new ArrayList<>(Objects.requireNonNull(aspects));
    }

    public boolean validate(Entity entity) {
        for(IAspect aspect : aspects)
            if(!aspect.validate(entity))
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

            aspects.add(IAspect.Include(RECAPTURE_TYPE_ARRAY(types)));
            return this;
        }

        public Builder exclude(Class<?>... types) {
            aspects.add(IAspect.Exclude(RECAPTURE_TYPE_ARRAY(types)));
            return this;
        }

        public EntityProfile build() {
            return new EntityProfile(aspects);
        }
    }

    @FunctionalInterface
    public static interface IAspect {
        public boolean validate(Entity entity);

        public static IAspect Include(Class<? extends IComponent>[] types) {
            return (entity) -> Arrays.stream(types).allMatch(entity::hasComponent);
        }

        public static IAspect Exclude(Class<? extends IComponent>[] types) {
            return (entity) -> Arrays.stream(types).noneMatch(entity::hasComponent);
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends IComponent>[] RECAPTURE_TYPE_ARRAY(Class<?>[] types) {
        Class<? extends IComponent>[] ctypes = new Class[types.length];
        for(int i = 0; i < types.length; i++) {
            if(IComponent.class.isAssignableFrom(types[i])) {
                ctypes[i] = (Class<? extends IComponent>) types[i];
            } else throw new RuntimeException("Invalid type : not a IComponent implementation");
        }
        return ctypes;
    }

}