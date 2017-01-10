package fr.hadriel.ecs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaDriel setOn 24/11/2016.
 */
public class EntityManager {

    private List<Entity> created;
    private List<Entity> entities;
    private List<Entity> destroyed;

    public EntityManager() {
        this.created = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.destroyed = new ArrayList<>();
    }

    public Entity get(long id) {
        Entity result = null;
        for(Entity e : entities) {
            if(e.id == id) {
                result = e;
                break;
            }
        }
        return result;
    }

    public void create(long id) {
        created.add(new Entity(id));
    }

    public void create(Entity e) {
        if(e == null) return;
        if(created.contains(e)) return;
        created.add(e);
    }

    public void destroy(Entity e) {
        if(e == null) return;
        if(destroyed.contains(e)) return;
        destroyed.add(e);
    }

    public void destroy(long id) {
        destroy(get(id));
    }

    public void clear() {
        created.clear();
        destroyed.clear();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void sync() {
        //Disallow duplicates, so destroy all old Entities that had the same id of an existing Entity
        for(Entity e : created) {
            Entity old = get(e.id);
            if(old != null) entities.remove(old);
        }
        entities.addAll(created);
        entities.removeAll(destroyed);
        created.clear();
        destroyed.clear();
    }
}
