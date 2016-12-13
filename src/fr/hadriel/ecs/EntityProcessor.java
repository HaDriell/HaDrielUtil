package fr.hadriel.ecs;

/**
 * Created by glathuiliere on 21/11/2016.
 */
public abstract class EntityProcessor {

    private final Class[] requiredTypes;

    protected EntityProcessor() {
        this.requiredTypes = requires();
    }

    public boolean accept(Entity entity) {
        for(Class c : requiredTypes) {
            if(entity.getComponent(c) == null)
                return false;
        }
        return true;
    }

    protected abstract Class[] requires();

    public abstract void update(Entity entity, float delta);
}