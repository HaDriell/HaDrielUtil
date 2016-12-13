package fr.hadriel.ecs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaDriel on 24/11/2016.
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

    public void add(long id) {
        created.add(new Entity(id));
    }

    public void add(Entity e) {
        if(e == null) return;
        created.add(e);
    }

    public void remove(Entity e) {
        if(e == null) return;
        destroyed.add(e);
    }

    public void remove(long id) {
        remove(get(id));
    }

    public void clear() {
        created.clear();
        destroyed.clear();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void update() {
        //Disallow duplicates, so remove all old Entities that had the same id of an existing Entity
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
