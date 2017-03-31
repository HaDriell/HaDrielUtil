package fr.hadriel.ecs.filter;

import fr.hadriel.ecs.EntityData;

/**
 * Created by glathuiliere on 31/03/2017.
 */
public final class Filters {
    private Filters() {}
    private static final IFilter ALWAYS_REFUSE = (data) -> false;
    private static final IFilter ALWAYS_ACCEPT = (data) -> true;

    public static final IFilter[] ALL = new IFilter[] { ALWAYS_ACCEPT };
    public static final IFilter[] NONE = new IFilter[] { ALWAYS_REFUSE };

    public static IFilter Require(String name, Class<?> type) {
        return new RequireFilter(name, type);
    }

    public static IFilter Exclude(String name, Class<?> type) {
        return new ExcludeFilter(name, type);
    }

    public static boolean accept(IFilter[] filters, EntityData entity) {
        for(IFilter filter : filters)
            if (!filter.accept(entity))
                return false;
        return true;
    }

    /* Common Filter implementations */

    private static final class RequireFilter implements IFilter {
        private final Class<?> type;
        private final String name;

        public RequireFilter(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public boolean accept(EntityData entity) {
            return entity.getComponent(name, type) != null;
        }
    }

    private static final class ExcludeFilter implements IFilter {
        private final Class<?> type;
        private final String name;

        public ExcludeFilter(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public boolean accept(EntityData entity) {
            return entity.getComponent(name, type) == null;
        }
    }
}