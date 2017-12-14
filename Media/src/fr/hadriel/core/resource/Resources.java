package fr.hadriel.core.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public final class Resources {

    public static boolean DO_LAZY_LOADING = true;

    private static final Map<Class<? extends AbstractResource>, Map<String, AbstractResource>> resources = new HashMap<>();

    private static Map<String, AbstractResource> map(Class<? extends AbstractResource> type) {
        return resources.computeIfAbsent(type, t -> new HashMap<>());
    }

    public static <R extends AbstractResource> R get(String name, Class<R> type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        AbstractResource resource = map(type).get(name);
        if(!resource.isInit()) resource.init(); // lazy loading call
        return type.cast(resource);
    }

    public static <R extends AbstractResource> void create(String name, R resource) { create(name, resource, DO_LAZY_LOADING); }
    public static <R extends AbstractResource> void create(String name, R resource, boolean lazyloading) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(resource);
        Class<? extends AbstractResource> type = resource.getClass();
        AbstractResource old = map(type).put(name, resource);
        old.destroy();
        if(!lazyloading) resource.init();
    }

    public static void delete(String name, Class<? extends AbstractResource> type) {
        if(name == null) throw new IllegalArgumentException("null arguments");
        if(type == null) throw new IllegalArgumentException("null arguments");
        AbstractResource old = map(type).remove(name);
        old.destroy();
    }
}