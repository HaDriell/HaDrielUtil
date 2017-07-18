package fr.hadriel.main.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glathuiliere on 10/01/2017.
 */
public class ClassResolver {

    private static final class Tag {
        public final long id;
        public final Class clazz;
        public Tag(long id, Class clazz) {
            this.id = id;
            this.clazz = clazz;
        }
    }

    private List<Tag> tags;

    public ClassResolver() {
        tags = new ArrayList<>();
    }

    public void register(long id, Class clazz) {
        //no duplicate id/clazz
        unregister(id);
        unregister(clazz);
        tags.add(new Tag(id, clazz));
    }

    public void unregister(long id) {
        tags.remove(getById(id));
    }

    public void unregister(Class clazz) {
        tags.remove(getByClass(clazz));
    }

    private Tag getById(long id) {
        for(Tag t : tags) {
            if(t.id == id) return t;
        }
        return null;
    }

    private Tag getByClass(Class clazz) {
        for(Tag t : tags) {
            if(t.clazz == clazz) return t;
        }
        return null;
    }

    public Class resolveClass(long id) {
        Tag t = getById(id);
        return t == null ? null : t.clazz;
    }

    public long resolveId(Class clazz) {
        Tag t = getByClass(clazz);
        return t == null ? 0 : t.id;
    }
}